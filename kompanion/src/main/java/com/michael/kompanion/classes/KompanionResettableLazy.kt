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
 */
class KompanionResettableLazy<T>(private val initializer: () -> T) {
    private var value: T? = null

    fun getValue(): T {
        if (value == null) {
            value = initializer()
        }
        return value!!
    }

    fun reset() {
        value = null
    }
}

/**
 * Creates a new instance of [KompanionResettableLazy] with the provided [initializer].
 *
 * ```Kt
 *
 * val lazyValue = resettableLazy { "Initialized" }
 *
 * println(lazyValue.getValue()) // Initialized
 *
 * lazyValue.reset()
 *
 * println(lazyValue.getValue()) // empty value again after reset
 * ```
 *
 */
fun <T> resettableLazy(initializer: () -> T): KompanionResettableLazy<T> {
    return KompanionResettableLazy(initializer)
}


