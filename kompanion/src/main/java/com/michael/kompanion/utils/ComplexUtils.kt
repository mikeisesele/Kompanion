import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


/**
 * Deep equals for data classes using reflection.
 *
 * ```Kt
 *
 * data class Person(val name: String, val age: Int, val friends: List<Person>)
 *
 * val person1 = Person("John", 25, listOf(Person("Doe", 30, emptyList())))
 * val person2 = Person("John", 25, listOf(Person("Doe", 30, emptyList())))
 *
 * println(deepEquals(person1, person2))  // Output: true
 *
 * ```
 *
 * @param obj1 The first object to compare.
 * @param obj2 The second object to compare.
 * @return True if both objects are deeply equal, otherwise false.
 */
fun <T : Any> kompanionDeepEquals(obj1: T?, obj2: T?): Boolean {
    if (obj1 == null || obj2 == null) return obj1 === obj2
    val kClass = obj1::class
    if (obj1::class != obj2::class) return false

    for (property in kClass.memberProperties) {
        val value1 = (property as KProperty1<T, *>).get(obj1)
        val value2 = property.get(obj2)

        if (value1 is List<*> && value2 is List<*>) {
            if (!value1.zip(value2).all { (v1, v2) -> kompanionDeepEquals(v1, v2) }) return false
        } else if (value1 is Map<*, *> && value2 is Map<*, *>) {
            if (!value1.keys.all { key -> kompanionDeepEquals(value1[key], value2[key]) }) return false
        } else if (value1 != value2) {
            return false
        }
    }
    return true
}

/**
 * Deep copy for data classes using reflection.
 *
 * ```Kt
 *
 * data class Address(val city: String)
 * data class Person(val name: String, val address: Address)
 *
 * val person1 = Person("John", Address("New York"))
 * val person2 = person1.deepCopy()
 *
 * person2.address.city = "San Francisco"
 *
 * println(person1) // Output: Person(name=John, address=Address(city=New York))
 * println(person2) // Output: Person(name=John, address=Address(city=San Francisco))
 *
 * ```
 *
 * @return A deep copy of the object.
 */
fun <T : Any> T.kompanionDeepCopyObject(): T {
    val kClass: KClass<T> = this::class as KClass<T>
    val constructor = kClass.primaryConstructor
        ?: throw IllegalArgumentException("Class must have a primary constructor")

    val args = constructor.parameters.map { param ->
        val value = kClass.memberProperties.first { it.name == param.name }.get(this)
        when (value) {
            is List<*> -> value.map { it?.deepCopy() }
            is Map<*, *> -> value.mapValues { it.value?.deepCopy() }
            is Set<*> -> value.map { it?.deepCopy() }.toSet()
            else -> value
        }
    }.toTypedArray()

    return constructor.call(*args)
}

/**
 * Extension function for deep copy of nullable types.
 *
 * @return A deep copy of the object or null if the original object is null.
 */
fun <T : Any> T?.deepCopy(): T? = this?.deepCopy()

/**
 * Command interface for actions.
 */
interface Command {
    fun execute()
}

/**
 * Extension function to convert a lambda into a Command.
 *
 * ```Kt
 *
 * val printCommand = { println("Print command executed!") }.toCommand()
 * val anotherCommand = { println("Another command!") }.toCommand()
 *
 * kompanionExecuteCommands(printCommand, anotherCommand)
 *
 * ```
 *
 * @return A Command instance that wraps the lambda.
 */
fun (() -> Unit).kompanionToCommand(): Command {
    return object : Command {
        override fun execute() {
            this@kompanionToCommand()
        }
    }
}

/**
 * Executes a series of Command instances.
 *
 * @param commands Vararg of Command instances to execute.
 */
fun kompanionExecuteCommands(vararg commands: Command) {
    commands.forEach { it.execute() }
}

/**
 * Memoizes a function with cache expiration after a specified timeout.
 *
 * @param timeout The duration after which the cache expires.
 * @param unit The time unit of the timeout duration.
 * @return A memoized version of the function.
 */
fun <T, R> ((T) -> R).kompanionMemoizeWithExpiry(timeout: Long, unit: TimeUnit): (T) -> R {
    val cache = ConcurrentHashMap<T, Pair<R, Long>>()
    val expiryTime = unit.toMillis(timeout)

    return { input: T ->
        val currentTime = System.currentTimeMillis()
        cache[input]?.takeIf { currentTime - it.second < expiryTime }?.first ?: run {
            val result = this(input)
            cache[input] = result to currentTime
            result
        }
    }
}

/**
 * Debounces a function, preventing it from being called too frequently.
 *
 *
 * ```Kt
 *
 * val debouncedPrint = { msg: String -> println(msg) }.debounce(1000L)
 * ```
 * @param waitMs The minimum wait time between consecutive calls.
 * @return A debounced version of the function.
 *
 */
fun <T> ((T) -> Unit).kompanionFunctionDebounce(waitMs: Long): (T) -> Unit {
    var lastInvocation = 0L
    return { param: T ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastInvocation >= waitMs) {
            lastInvocation = currentTime
            this(param)
        }
    }
}

/**
 * Rate limits a function to only allow execution every `intervalMs` milliseconds.
 *
 * ```Kt
 * val rateLimitedPrint = { msg: String -> println(msg) }.rateLimit(1000L)
 * ```
 *
 * @param intervalMs The time interval between allowed executions.
 * @return A rate-limited version of the function.
 *
 */
fun <T> ((T) -> Unit).kompanionRateLimit(intervalMs: Long): (T) -> Unit {
    var lastInvocation = 0L

    return { param: T ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastInvocation >= intervalMs) {
            lastInvocation = currentTime
            this(param)
        }
    }
}

/**
 * Throttles a suspending function to only allow execution once per `intervalMs` milliseconds.
 *
 * ```Kt
 *
 * val throttledPrint = { msg: String -> println(msg) }.throttle(1000L)
 * ```
 * @param intervalMs The time interval between allowed executions.
 * @return A throttled version of the suspending function.
 *
 */
fun <T> ((T) -> Unit).kompanionThrottle(intervalMs: Long): (T) -> Unit {
    var lastInvocation = 0L
    val lock = Any()

    return { param: T ->
        synchronized(lock) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastInvocation >= intervalMs) {
                lastInvocation = currentTime
                this(param)
            }
        }
    }
}

/**
 * Coroutine version of throttle for suspending functions.
 *
 * ```Kt
 *
 * val throttledPrintCoroutine = { msg: String -> println(msg) }.throttleCoroutine(1000L)
 * ```
 *
 * @param intervalMs The time interval between allowed executions.
 * @return A throttled coroutine version of the function.
 *
 */
fun <T> ((T) -> Unit).kompanionThrottleCoroutine(intervalMs: Long): suspend (T) -> Unit {
    var lastInvocation = 0L
    val lock = Any()

    return { param: T ->
        synchronized(lock) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastInvocation >= intervalMs) {
                lastInvocation = currentTime
                this(param)
            }
        }
    }
}
