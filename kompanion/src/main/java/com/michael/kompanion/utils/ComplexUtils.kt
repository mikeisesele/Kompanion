import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * Deep equals for data classes using reflection.
 *
 * When to use:
 * - Use this utility when comparing complex data structures, such as nested data classes, for deep equality.
 * - Suitable when you want to ensure that all properties of two objects, including nested objects, are the same.
 *
 * Example:
 * ```Kt
 * data class Person(val name: String, val age: Int, val friends: List<Person>)
 *
 * val person1 = Person("John", 25, listOf(Person("Doe", 30, emptyList())))
 * val person2 = Person("John", 25, listOf(Person("Doe", 30, emptyList())))
 *
 * println(kompanionDeepEquals(person1, person2))  // Output: true
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
 * When to use:
 * - Use this utility to make a deep copy of an object, duplicating all nested objects and lists.
 * - This is useful when you need to create a copy of an object that won’t affect the original.
 *
 * Example:
 * ```Kt
 * data class Address(val city: String)
 * data class Person(val name: String, val address: Address)
 *
 * val person1 = Person("John", Address("New York"))
 * val person2 = person1.kompanionDeepCopyObject()
 *
 * person2.address.city = "San Francisco"
 *
 * println(person1) // Output: Person(name=John, address=Address(city=New York))
 * println(person2) // Output: Person(name=John, address=Address(city=San Francisco))
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
fun <T : Any> T?.deepCopy(): T? = this?.kompanionDeepCopyObject()


/**
 * Memoizes a function with cache expiration after a specified timeout.
 *
 * When to use:
 * - Use this to cache the result of a function call for a certain duration, to avoid redundant calculations or API calls.
 *
 * Example:
 * ```Kt
 * val memoizedFunction = { x: Int -> x * 2 }.kompanionMemoizeWithExpiry(5, TimeUnit.MINUTES)
 * ```
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
 * When to use:
 * - Use this to prevent excessive calls to a function within a short time frame, e.g., avoiding multiple button clicks.
 *
 * Example:
 * ```Kt
 * val debouncedPrint = { msg: String -> println(msg) }.kompanionFunctionDebounce(1000L)
 * ```
 * @param waitMs The minimum wait time between consecutive calls.
 * @return A debounced version of the function.
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
 * Debounces a click event handler, preventing it from being called too frequently.
 *
 * When to use:
 * - Use this to debounce button clicks, avoiding accidental multiple activations in quick succession.
 *
 * Example:
 * ```Kt
 * val debouncedClick = { println("Click") }.kompanionDebounce(500L)
 * ```
 * @param waitMs The minimum wait time between consecutive clicks.
 * @return A debounced version of the click handler.
 */
fun (() -> Unit).kompanionDebounce(waitMs: Long): () -> Unit {
    var lastInvocation = 0L
    return {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastInvocation >= waitMs) {
            lastInvocation = currentTime
            this()
        }
    }
}

/**
 * Rate limits a function to only allow execution every `intervalMs` milliseconds.
 *
 * When to use:
 * - Use this to throttle high-frequency calls to a function, ensuring it doesn't run more often than desired.
 *
 * Example:
 * ```Kt
 * val rateLimitedPrint = { msg: String -> println(msg) }.kompanionRateLimit(1000L)
 * ```
 * @param intervalMs The time interval between allowed executions.
 * @return A rate-limited version of the function.
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
