package com.michael.kompanion.utils

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.math.round
import kotlin.random.Random

/**
* Checks if all the provided objects are not null.
*
* @param objects List of objects to check.
* @return `true` if all objects are not null, `false` otherwise.
*/
fun <T> kompanionAllNotNull(vararg objects: T?): Boolean {
    return objects.all { it != null }
}

/**
 * Checks if any of the provided objects is null.
 *
 * @param objects List of objects to check.
 * @return `true` if any object is null, `false` otherwise.
 */
fun <T> kompanionAnyIsNull(vararg objects: T?): Boolean {
    return objects.any { it == null }
}

/**
 * Clears all elements in the provided arrays.
 * Note: This function expects the objects to be ArrayLists.
 *
 * @param objects List of ArrayLists to clear.
 */
fun <T> kompanionClearAllArray(vararg objects: T?) {
    objects.forEach { (it as ArrayList<*>).clear() }
}

/**
 * Extension function to set a default value if the object is null.
 *
 * @param defaultValue A lambda function providing the default value.
 * @return The original object if not null, otherwise the result of defaultValue().
 */
inline fun <T> T?.kompanionIfNullSetDefault(defaultValue: () -> T): T {
    return this ?: defaultValue()
}

/**
 * Extension function to perform an operation if the object is null.
 *
 * @param operation A lambda function representing the operation to perform if the object is null.
 */
inline fun <T> T?.kompanionIfNullPerform(operation: () -> Unit) {
    this ?: operation()
}


/**
 * Generates a random integer within the specified range.
 */
fun kompanionRandomInt(min: Int, max: Int): Int = (min..max).random()

/**
 * Creates a shallow copy of the set.
 */
fun <T> Set<T>.kompanionShallowCopy(): Set<T> = HashSet(this)

/**
 * Creates a shallow copy of the map.
 */
fun <K, V> Map<K, V>.kompanionShallowCopy(): Map<K, V> = HashMap(this)

/**
 * Calculates the factorial of a non-negative integer.
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
 */
inline fun <K, V> Map<K, V?>.kompanionGetOrDefault(key: K, defaultValue: () -> V): V {
    return this[key] ?: defaultValue()
}

/**
 * Executes the block if all provided arguments are not null.
 */
inline fun <T1, T2, R> kompanionRunIfNotNull(arg1: T1?, arg2: T2?, block: (T1, T2) -> R): R? {
    return if (arg1 != null && arg2 != null) block(arg1, arg2) else null
}

/**
 * Runs the block if all provided values are non-null.
 */
inline fun <T1, T2, T3, R> kompanionIfAllNotNull(arg1: T1?, arg2: T2?, arg3: T3?, block: (T1, T2, T3) -> R): R? {
    return if (arg1 != null && arg2 != null && arg3 != null) block(arg1, arg2, arg3) else null
}

/**
 * Swaps two elements in a mutable list.
 */
fun <T> MutableList<T>.kompanionSwap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

/**
 * Safely casts the object to the given type or returns the default value.
 */
inline fun <reified T> Any?.kompanionCastOrDefault(defaultValue: () -> T): T {
    return this as? T ?: defaultValue()
}

/**
 * Initializes the value if it's null.
 */
inline fun <T> T?.kompanionInitializeIfNull(initializer: () -> T): T {
    return this ?: initializer()
}


/**
 * Checks if all strings in the collection are non-null and non-empty.
 */
fun Collection<String?>.kompanionAllNotEmpty(): Boolean {
    return this.all { !it.isNullOrEmpty() }
}

/**
 * Runs an action for each non-null element in a collection.
 */
inline fun <T> Collection<T?>.kompanionForEachNotNull(action: (T) -> Unit) {
    for (element in this) {
        element?.let(action)
    }
}


/**
 * Checks if a string is a palindrome.
 */
fun kompanionIsPalindrome(str: String): Boolean = str == str.reversed()

/*
 * generate a random float number within a given range
 */
fun kompanionFloatRandom(min: Float, max: Float): Float {
    val randomValue = Random.nextFloat() * (max - min) + min
    return round(randomValue * 10) / 10
}

/**
 * Retries the given block up to [times] if it throws an exception.
 * @param times The number of times to retry.
 * @param block The block of code to retry.
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
 */
fun Boolean?.kompanionOrFalse(): Boolean {
    return this ?: false
}

/**
 * Converts a nullable boolean to true if it's null.
 */
fun Boolean?.kompanionOrTrue(): Boolean {
    return this ?: true
}

/**
 * Converts a nullable string to an empty string if it's null.
 */
fun String?.kompanionOrEmpty(): String {
    return this ?: ""
}

/**
 * Measures and prints the time taken by a block to execute.
 *
 * @param tag An optional tag to identify the execution time log.
 * @param block The block of code to measure.
 * @return The result of the block execution.
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
 *
 * @param keys The list of keys to check for non-null values.
 * @param block The block to execute if all specified keys have non-null values.
 * @return The result of the block execution, or null if not all keys have non-null values.
 */
inline fun <K, V, R> Map<K, V?>.kompanionWithNonNullValues(keys: List<K>, block: (Map<K, V>) -> R): R? {
    val nonNullValues = keys.mapNotNull { key -> this[key]?.let { key to it } }.toMap()
    return if (nonNullValues.size == keys.size) block(nonNullValues) else null
}

/**
 * Limits the length of a string, appending an ellipsis if the limit is exceeded.
 *
 * @param maxLength The maximum allowed length of the string.
 * @param ellipsis The string to append if the length limit is exceeded.
 * @return The limited length string, possibly with an ellipsis.
 */
fun String.kompanionLimitLength(maxLength: Int, ellipsis: String = "..."): String {
    return if (this.length > maxLength) this.take(maxLength) + ellipsis else this
}

/**
 * Repeatedly runs a block until the condition is met.
 *
 * @param condition The condition to check.
 * @param block The block to run until the condition is met.
 */
inline fun kompanionRunUntil(condition: () -> Boolean, block: () -> Unit) {
    while (!condition()) {
        block()
    }
}

/**
 * Executes the block if the value has changed since the last check.
 *
 * @param newValue The new value to check against the current value.
 * @param action The action to execute if the value has changed.
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
 * Executes the block if any element in the collection is null.
 *
 * @param block The block to execute if any element is null.
 */
inline fun <T> Collection<T?>.kompanionIfAnyIsNull(block: () -> Unit) {
    if (any { it == null }) {
        block()
    }
}

/**
 * Filters the collection using multiple predicates.
 *
 * @param predicates The predicates to filter the collection by.
 * @return A list of items that match all provided predicates.
 */
fun <T> Iterable<T>.kompanionFilterWithPredicates(vararg predicates: (T) -> Boolean): List<T> {
    return this.filter { item -> predicates.all { it(item) } }
}

/**
 * Executes different blocks based on the type of the object.
 *
 * @param block The block to execute if the object matches the specified type.
 */
inline fun <reified T> Any.kompanionRunIfTypeMatches(block: (T) -> Unit) {
    if (this is T) {
        block(this)
    }
}

/**
 * Ensures that the block is executed only once.
 *
 * @param block The block to execute.
 * @return A function that can be called to execute the block once.
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
 *
 * @param blocks The pairs of conditions and blocks to evaluate.
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
 *
 * @param block The block to execute upon removal of an element.
 * @return A mutable list that triggers the block on removal.
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
 *
 * @param block The block to execute upon modification.
 * @return A mutable collection that triggers the block on modification.
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
 *
 * @param transform The transformation function to apply to the pair.
 * @return The result of the transformation.
 */
inline fun <A, B, R> Pair<A, B>.kompanionMapTo(transform: (A, B) -> R): R {
    return transform(first, second)
}

/**
 * Toggles the Boolean value.
 *
 * @return The toggled Boolean value.
 */
fun Boolean.kompanionToggle(): Boolean = !this

/**
 * Inline function to capture a reified generic type.
 *
 * @return The Class instance of the reified type.
 */
inline fun <reified T> kompanionClassOf(): Class<T> = T::class.java

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
