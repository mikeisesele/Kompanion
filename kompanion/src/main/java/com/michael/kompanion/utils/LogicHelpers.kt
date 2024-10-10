package com.michael.kompanion.utils

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.math.round
import kotlin.random.Random


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */



/**
 * Checks if all the provided objects are not null.
 *
 * Example usage:
 *
 * ```Kt
 *
 * val areAllNotNull = kompanionAllNotNull("Hello", 123, true)
 * println(areAllNotNull) // true
 * ```
 */
fun <T> kompanionAllNotNull(vararg objects: T?): Boolean {
    return objects.all { it != null }
}

/**
 * Checks if any of the provided objects is null.
 *
 * Example usage:
 *
 * ```Kt
 *
 * val anyNull = kompanionAnyIsNull("Hello", null, true)
 * println(anyNull) // true
 * ```
 */
fun <T> kompanionAnyIsNull(vararg objects: T?): Boolean {
    return objects.any { it == null }
}

/**
 * Clears all elements in the provided arrays.
 * Example usage:
 *
 * ```Kt
 *
 * val list1 = arrayListOf(1, 2, 3)
 * val list2 = arrayListOf("A", "B", "C")
 * kompanionClearAllArray(list1, list2)
 * println(list1) // []
 * println(list2) // []
 * ```
 */
fun <T> kompanionClearAllArray(vararg objects: T?) {
    objects.forEach { (it as ArrayList<*>).clear() }
}

/**
 * Extension function to set a default value if the object is null.
 * Example usage:
 *
 * ```Kt
 *
 * val name: String? = null
 * val result = name.kompanionIfNullSetDefault { "Default Name" }
 * println(result) // "Default Name"
 * ```
 */
inline fun <T> T?.kompanionIfNullSetDefault(defaultValue: () -> T): T {
    return this ?: defaultValue()
}

/**
 * Extension function to perform an operation if the object is null.
 * Example usage:
 *
 * ```Kt
 *
 * val nullableValue: Int? = null
 * nullableValue.kompanionIfNullPerform { println("Value is null!") } // Value is null!
 * ```
 */
inline fun <T> T?.kompanionIfNullPerform(operation: () -> Unit) {
    this ?: operation()
}

/**
 * Generates a random integer within the specified range.
 * Example usage:
 *
 * ```Kt
 *
 * val randomInt = kompanionRandomInt(1, 10)
 * println(randomInt) // A random number between 1 and 10 (e.g., 7)
 * ```
 */
fun kompanionRandomInt(min: Int, max: Int): Int = (min..max).random()

/**
 * Creates a shallow copy of the set.
 * Example usage:
 *
 * ```Kt
 *
 * val originalSet = setOf(1, 2, 3)
 * val copySet = originalSet.kompanionShallowCopy()
 * println(copySet) // [1, 2, 3]
 * ```
 */
fun <T> Set<T>.kompanionShallowCopy(): Set<T> = HashSet(this)

/**
 * Creates a shallow copy of the map.
 * Example usage:
 *
 * ```Kt
 *
 * val originalMap = mapOf(1 to "One", 2 to "Two")
 * val copyMap = originalMap.kompanionShallowCopy()
 * println(copyMap) // {1=One, 2=Two}
 * ```
 */
fun <K, V> Map<K, V>.kompanionShallowCopy(): Map<K, V> = HashMap(this)

/**
 * Calculates the factorial of a non-negative integer.
 * Example usage:
 *
 * ```Kt
 *
 * val factorial = kompanionFactorial(5)
 * println(factorial) // 120
 * ```
 */
fun kompanionFactorial(n: Int): Long {
    require(n >= 0) { "Factorial is defined only for non-negative integers." }
    var result = 1L
    for (i in 2..n) {
        result *= i
    }
    return result
}

/**
 * Retrieves the value for the given key or returns the default value if the key doesn't exist or the value is null.
 * Example usage:
 *
 * ```Kt
 *
 * val map = mapOf(1 to "One", 2 to null)
 * val value = map.kompanionGetOrDefault(2) { "Default Value" }
 * println(value) // "Default Value"
 * ```
 */
inline fun <K, V> Map<K, V?>.kompanionGetOrDefault(key: K, defaultValue: () -> V): V {
    return this[key] ?: defaultValue()
}

/**
 * Executes the block if all provided arguments are not null.
 * Example usage:
 *
 * ```Kt
 *
 * val result = kompanionRunIfNotNull("Hello", 123) { str, num -> "$str $num" }
 * println(result) // "Hello 123"
 * ```
 */
inline fun <T1, T2, R> kompanionRunIfNotNull(arg1: T1?, arg2: T2?, block: (T1, T2) -> R): R? {
    return if (arg1 != null && arg2 != null) block(arg1, arg2) else null
}

/**
 * Runs the block if all provided values are non-null.
 * Example usage:
 *
 * ```Kt
 *
 * val result = kompanionIfAllNotNull("Hello", 123, true) { str, num, bool -> "$str $num $bool" }
 * println(result) // "Hello 123 true"
 * ```
 */
inline fun <T1, T2, T3, R> kompanionIfAllNotNull(arg1: T1?, arg2: T2?, arg3: T3?, block: (T1, T2, T3) -> R): R? {
    return if (arg1 != null && arg2 != null && arg3 != null) block(arg1, arg2, arg3) else null
}

/**
 * Swaps two elements in a mutable list.
 * Example usage:
 *
 * ```Kt
 *
 * val list = mutableListOf(1, 2, 3)
 * list.kompanionSwap(0, 2)
 * println(list) // [3, 2, 1]
 * ```
 */
fun <T> MutableList<T>.kompanionSwap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

/**
 * Safely casts the object to the given type or returns the default value.
 * Example usage:
 *
 * ```Kt
 *
 * val obj: Any = "Hello"
 * val result = obj.kompanionCastOrDefault { "Default" }
 * println(result) // "Hello"
 * ```
 */
inline fun <reified T> Any?.kompanionCastOrDefault(defaultValue: () -> T): T {
    return this as? T ?: defaultValue()
}

/**
 * Initializes the value if it's null.
 * Example usage:
 *
 * ```Kt
 *
 * val name: String? = null
 * val result = name.kompanionInitializeIfNull { "Default Name" }
 * println(result) // "Default Name"
 * ```
 */
inline fun <T> T?.kompanionInitializeIfNull(initializer: () -> T): T {
    return this ?: initializer()
}

/**
 * Checks if all strings in the collection are non-null and non-empty.
 * Example usage:
 *
 * ```Kt
 *
 * val strings = listOf("A", "B", "")
 * val allNotEmpty = strings.kompanionAllNotEmpty()
 * println(allNotEmpty) // false
 * ```
 */
fun Collection<String?>.kompanionAllNotEmpty(): Boolean {
    return this.all { !it.isNullOrEmpty() }
}

/**
 * Runs an action for each non-null element in a collection.
 * Example usage:
 *
 * ```Kt
 *
 * val list = listOf(1, null, 2)
 * list.kompanionForEachNotNull { println(it) }
 * // 1
 * // 2
 * ```
 */
inline fun <T> Collection<T?>.kompanionForEachNotNull(action: (T) -> Unit) {
    for (element in this) {
        element?.let(action)
    }
}

/**
 * Checks if a string is a palindrome.
 * Example usage:
 *
 * ```Kt
 *
 * val isPalindrome = kompanionIsPalindrome("madam")
 * println(isPalindrome) // true
 * ```
 */
fun kompanionIsPalindrome(str: String): Boolean = str == str.reversed()

/**
 * Generates a random float number within a given range.
 * Example usage:
 *
 * ```Kt
 *
 * val randomFloat = kompanionFloatRandom(1.0f, 10.0f)
 * println(randomFloat) // e.g., 7.5
 * ```
 */
fun kompanionFloatRandom(min: Float, max: Float): Float {
    val randomValue = Random.nextFloat() * (max - min) + min
    return round(randomValue * 10) / 10
}

/**
 * Retries the given block up to [times] if it throws an exception.
 * Example usage:
 *
 * ```Kt
 *
 * val result = kompanionRetry(3) {
 *     if (Random.nextBoolean()) throw Exception("Error")
 *     "Success"
 * }
 * println(result) // "Success" (after retries if necessary)
 * ```
 */
inline fun <T> kompanionRetry(times: Int, block: () -> T): T? {
    var currentAttempt = 0
    while (currentAttempt < times) {
        try {
            return block()
        } catch (e: Exception) {
            currentAttempt++
        }
    }
    return null
}

/**
 * Converts a nullable boolean to false if it's null.
 * Example usage:
 *
 * ```Kt
 *
 * val bool: Boolean? = null
 * val result = bool.kompanionOrFalse()
 * println(result) // false
 * ```
 */
fun Boolean?.kompanionOrFalse(): Boolean {
    return this ?: false
}

/**
 * Converts a nullable boolean to true if it's null.
 * Example usage:
 *
 * ```Kt
 *
 * val bool: Boolean? = null
 * val result = bool.kompanionOrTrue()
 * println(result) // true
 * ```
 */
fun Boolean?.kompanionOrTrue(): Boolean {
    return this ?: true
}

/**
 * Converts a nullable string to an empty string if it's null.
 * Example usage:
 *
 * ```Kt
 *
 * val str: String? = null
 * val result = str.kompanionOrEmpty()
 * println(result) // ""
 * ```
 */
fun String?.kompanionOrEmpty(): String {
    return this ?: ""
}

/**
 * Measures and prints the time taken by a block to execute.
 * Example usage:
 *
 * ```Kt
 *
 * val result = kompanionMeasureExecutionTime("Time") {
 *     Thread.sleep(100)
 *     "Finished"
 * }
 * println(result) // "Finished"
 * ```
 */
inline fun <T> kompanionMeasureExecutionTime(tag: String = "ExecutionTime", block: () -> T): T {
    val start = System.currentTimeMillis()
    val result = block()
    val end = System.currentTimeMillis()
    val time = "$tag took ${end - start}ms"
    println(time)
    return result
}

/**
 * Executes the block if all specified keys in the map have non-null values.
 * Example usage:
 *
 * ```Kt
 *
 * val map = mapOf(1 to "One", 2 to "Two", 3 to null)
 * val result = map.kompanionWithNonNullValues(listOf(1, 2)) { it.values.joinToString() }
 * println(result) // "One, Two"
 * ```
 */
inline fun <K, V, R> Map<K, V?>.kompanionWithNonNullValues(keys: List<K>, block: (Map<K, V>) -> R): R? {
    val nonNullValues = keys.mapNotNull { key -> this[key]?.let { key to it } }.toMap()
    return if (nonNullValues.size == keys.size) block(nonNullValues) else null
}

/**
 * Limits the length of a string, appending an ellipsis if the limit is exceeded.
 * Example usage:
 *
 * ```Kt
 *
 * val result = "Hello World!".kompanionLimitLength(5)
 * println(result) // "Hello..."
 * ```
 */
fun String.kompanionLimitLength(maxLength: Int, ellipsis: String = "..."): String {
    return if (this.length > maxLength) this.take(maxLength) + ellipsis else this
}

/**
 * Repeatedly runs a block until the condition is met.
 * Example usage:
 *
 * ```Kt
 *
 * var count = 0
 * kompanionRunUntil({ count == 5 }) {
 *     println("Count is $count")
 *     count++
 * }
 * // Count is 0
 * // Count is 1
 * // Count is 2
 * // Count is 3
 * // Count is 4
 * ```
 */
inline fun kompanionRunUntil(condition: () -> Boolean, block: () -> Unit) {
    while (!condition()) {
        block()
    }
}

/**
 * Executes the block if the value has changed since the last check.
 * Example usage:
 *
 * ```Kt
 *
 * val tracker = ValueTracker(1)
 * tracker.onValueChange(2) { println("Value changed to $it") } // Value changed to 2
 * ```
 */
class ValueTracker<T>(private var value: T) {
    fun onValueChange(newValue: T, action: (T) -> Unit) {
        if (newValue != value) {
            value = newValue
            action(newValue)
        }
    }
}

/**
 * Executes a block if any element in the collection is null.
 * Example usage:
 *
 * ```Kt
 *
 * val list = listOf(1, null, 2)
 * list.kompanionIfAnyIsNull { println("Null element found!") } // Null element found!
 * ```
 */
inline fun <T> Collection<T?>.kompanionIfAnyIsNull(block: () -> Unit) {
    if (any { it == null }) {
        block()
    }
}

/**
 * Filters the collection using multiple predicates.
 * Example usage:
 *
 * ```Kt
 *
 * val list = listOf(1, 2, 3, 4)
 * val result = list.kompanionFilterWithPredicates({ it > 1 }, { it < 4 })
 * println(result) // [2, 3]
 * ```
 */
fun <T> Iterable<T>.kompanionFilterWithPredicates(vararg predicates: (T) -> Boolean): List<T> {
    return this.filter { item -> predicates.all { it(item) } }
}

/**
 * Executes different blocks based on the type of the object.
 * Example usage:
 *
 * ```Kt
 *
 * val obj: Any = "Hello"
 * obj.kompanionRunIfTypeMatches<String> { println("String: $it") } // String: Hello
 * ```
 */
inline fun <reified T> Any.kompanionRunIfTypeMatches(block: (T) -> Unit) {
    if (this is T) {
        block(this)
    }
}

/**
 * Ensures that the block is executed only once.
 * Example usage:
 *
 * ```Kt
 *
 * val runOnce = kompanionRunOnce { println("Running once") }
 * runOnce() // Running once
 * runOnce() // Doesn't execute again
 * ```
 */
inline fun kompanionRunOnce(crossinline block: () -> Unit): () -> Unit {
    var isExecuted = false
    return {
        if (!isExecuted) {
            block()
            isExecuted = true
        }
    }
}

/**
 * Lazily evaluates multiple blocks and runs the first one whose condition is true.
 * Example usage:
 *
 * ```Kt
 *
 * kompanionLazyEvaluate(
 *     true to { println("This is true!") } // This is true!
 * )
 * ```
 */
fun kompanionLazyEvaluate(vararg blocks: Pair<Boolean, () -> Unit>) {
    for ((condition, block) in blocks) {
        if (condition) {
            block()
            return
        }
    }
}

/**
 * Executes a block when an element is removed from the list.
 * Example usage:
 *
 * ```Kt
 *
 * val list = mutableListOf(1, 2, 3)
 * val result = list.kompanionOnRemove { println("$it was removed!") }
 * result.remove(2) // 2 was removed!
 * ```
 */
inline fun <T> MutableList<T>.kompanionOnRemove(crossinline block: (T) -> Unit): MutableList<T> {
    return object : MutableList<T> by this {
        override fun remove(element: T): Boolean {
            val result = this@kompanionOnRemove.remove(element)
            if (result) block(element)
            return result
        }
    }
}

/**
 * Executes a block after adding or removing elements from the collection.
 * Example usage:
 *
 * ```Kt
 *
 * val list = mutableListOf(1, 2, 3)
 * val result = list.kompanionOnModification { println("List modified!") }
 * result.add(4) // List modified!
 * result.remove(1) // List modified!
 * ```
 */
inline fun <T> MutableCollection<T>.kompanionOnModification(crossinline block: () -> Unit): MutableCollection<T> {
    return object : MutableCollection<T> by this {
        override fun add(element: T): Boolean {
            val result = this@kompanionOnModification.add(element)
            block()
            return result
        }

        override fun remove(element: T): Boolean {
            val result = this@kompanionOnModification.remove(element)
            block()
            return result
        }
    }
}

/**
 * Transforms a pair into a new instance of another type.
 * Example usage:
 *
 * ```Kt
 *
 * val pair = 1 to "one"
 * val result = pair.kompanionMapTo { a, b -> "$a is $b" }
 * println(result) // "1 is one"
 * ```
 */
inline fun <A, B, R> Pair<A, B>.kompanionMapTo(transform: (A, B) -> R): R {
    return transform(first, second)
}

/**
 * Toggles the Boolean value.
 * Example usage:
 *
 * ```Kt
 *
 * val bool = true
 * val result = bool.kompanionToggle()
 * println(result) // false
 * ```
 */
fun Boolean.kompanionToggle(): Boolean = !this

/**
 * Extension function to get the class of an object.
 *
 * Example usage:
 *
 * ```Kt
 *
 * val str = "Hello"
 * val classType = str.kompanionGetInstance()
 * println(classType.simpleName) // String
 * ```
 */
fun Any.kompanionGetInstance(): Class<*> {
    return this::class.java
}


/**
 * Retries an operation with exponential backoff.
 *
 * @param initialDelay The initial delay before the first retry.
 * @param maxRetries The maximum number of retry attempts.
 * @param factor The multiplier for the delay after each retry.
 * @param operation The operation to retry.
 * @return The result of the operation.
 * @throws Exception If all retries fail.
 *
 * Example usage:
 *
 * ```Kt
 *
 * val result = retryWithBackoff {
 *     // Some potentially failing operation
 *     println("Attempting operation...")
 *     if (Math.random() > 0.5) {
 *         throw Exception("Failed attempt!")
 *     }
 *     "Success!"
 * }
 *
 * println(result) // Retries with backoff on failure
 *
 * ```
 */
inline fun <T> kompanionRetryWithBackoff(
    initialDelay: Long = 1000L,
    maxRetries: Int = 3,
    factor: Double = 2.0,
    operation: () -> T
): T {
    var currentDelay = initialDelay
    repeat(maxRetries - 1) {
        try {
            return operation()
        } catch (e: Exception) {
            Thread.sleep(currentDelay)
            currentDelay = (currentDelay * factor).toLong()
        }
    }
    return operation() // Final attempt without catching
}

/**
 * Handles behavior based on the state.
 *
 * @param onIdle Action to execute when the state is IDLE.
 * @param onLoading Action to execute when the state is LOADING.
 * @param onSuccess Action to execute when the state is SUCCESS.
 * @param onError Action to execute when the state is ERROR.
 */
enum class KompanionState {
    IDLE, LOADING, SUCCESS, ERROR
}

/**
 * Handles behavior based on the state.
 *
 * @param onIdle Action to execute when the state is IDLE.
 * @param onLoading Action to execute when the state is LOADING.
 * @param onSuccess Action to execute when the state is SUCCESS.
 * @param onError Action to execute when the state is ERROR.
 */
inline fun KompanionState.handleState(
    onIdle: () -> Unit = {},
    onLoading: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onError: () -> Unit = {}
) {
    when (this) {
        KompanionState.IDLE -> onIdle()
        KompanionState.LOADING -> onLoading()
        KompanionState.SUCCESS -> onSuccess()
        KompanionState.ERROR -> onError()
    }
}
