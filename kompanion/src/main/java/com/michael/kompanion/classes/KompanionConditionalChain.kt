package com.michael.kompanion.classes

/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * Chains multiple conditional actions based on different predicates.
 *
 * This class allows you to create a series of conditions (similar to an if-else chain) for an object.
 * Once a condition is met, subsequent conditions are skipped, and the chain stops evaluating.
 *
 * @param target The object to be evaluated against the conditions.
 */
class KompanionConditionalChain<T>(val target: T) {
    var result: Boolean = false

    /**
     * Applies the given block if the predicate is true.
     * If a condition has already been met (i.e., `result` is true), this method does nothing.
     *
     * @param predicate A condition to check against the target.
     * @param block The action to execute if the condition is met.
     * @return The current chain for further condition checks.
     */
    inline fun ifCondition(predicate: (T) -> Boolean, block: (T) -> Unit): KompanionConditionalChain<T> {
        if (!result && predicate(target)) {
            block(target)
            result = true
        }
        return this
    }

    /**
     * Executes the provided block if no previous condition was met.
     *
     * @param block The action to execute if no conditions were met.
     * @return The current chain for further condition checks.
     */
    inline fun elseCondition(block: (T) -> Unit): KompanionConditionalChain<T> {
        if (!result) {
            block(target)
        }
        return this
    }
}

/**
 * Starts a conditional chain for an object.
 *
 * This extension function allows you to start a conditional chain on any object. You can chain multiple
 * `ifCondition` and `elseCondition` calls to handle different conditions for the object.
 *
 * Example usage:
 *
 * ```Kt
 *
 * val num = 5
 *
 * num
 * .kompanionConditionalChain()
 * .ifCondition({ it > 10 }) { println("Greater than 10") }
 * .ifCondition({ it == 5 }) { println("Equal to 5") }
 * .elseCondition { println("None of the conditions met") }
 * ```
 *
 * Output:
 * "Equal to 5"
 *
 * @return A KompanionConditionalChain initialized with the object as the target.
 */
fun <T> T.kompanionConditionalChain(): KompanionConditionalChain<T> = KompanionConditionalChain(this)
