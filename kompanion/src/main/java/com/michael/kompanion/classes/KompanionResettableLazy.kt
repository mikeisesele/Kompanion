package com.michael.kompanion.classes

/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * A lazy value that can be reset.
 *
 * This class is similar to Kotlin's standard lazy delegate, but with an added ability to reset the value.
 * Once the value is initialized, it can be retrieved using the `getValue()` method, and it will persist until it is manually reset using the `reset()` method.
 *
 * Example usage:
 *
 * ```Kt
 *
 * val lazyValue = KompanionResettableLazy { "Initialized" }
 *
 * println(lazyValue.getValue()) // Prints: Initialized
 *
 * lazyValue.reset() // Reset the lazy value
 *
 * println(lazyValue.getValue()) // Reinitializes the value and prints: Initialized
 * ```
 *
 * When to use:
 * Use this class when you want to lazily initialize a value and have the ability to reset it later on, triggering a re-initialization when accessed again.
 * It's useful in scenarios where you need a cached value but want the ability to reset the cache and reload it.
 *
 * @param initializer A lambda function that initializes the value when first accessed.
 */
class KompanionResettableLazy<T>(private val initializer: () -> T) {
    private var value: T? = null

    /**
     * Retrieves the value, initializing it if necessary.
     *
     * Example usage:
     *
     * ```Kt
     *
     * val lazyValue = KompanionResettableLazy { "Lazy Initialization" }
     * println(lazyValue.getValue()) // Prints: Lazy Initialization
     * ```
     *
     * When to use:
     * Call this method when you want to access the lazily initialized value. If the value is already initialized,
     * it will return the cached value. Otherwise, it will call the initializer to create the value.
     *
     * @return The initialized value.
     */
    fun getValue(): T {
        if (value == null) {
            value = initializer()
        }
        return value!!
    }

    /**
     * Resets the lazily initialized value, allowing it to be reinitialized on the next call to `getValue()`.
     *
     * When to use:
     * Use this method when you need to clear or reset the value, forcing it to be reinitialized the next time `getValue()` is called.
     */
    fun reset() {
        value = null
    }
}

/**
 * Creates a new instance of [KompanionResettableLazy] with the provided [initializer].
 *
 * When to use:
 * Use this factory function to create instances of `KompanionResettableLazy`. It provides a cleaner way to create lazily initialized values that can be reset.
 * It's useful in cases where you want to lazily initialize an object, but retain the option to reset and reinitialize it later.
 *
 * @param initializer A lambda function that initializes the value when first accessed.
 * @return A new instance of `KompanionResettableLazy`.
 */
fun <T> resettableLazy(initializer: () -> T): KompanionResettableLazy<T> {
    return KompanionResettableLazy(initializer)
}
