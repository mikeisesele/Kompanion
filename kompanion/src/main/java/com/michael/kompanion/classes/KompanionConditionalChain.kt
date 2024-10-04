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
 */
class KompanionConditionalChain<T>(val target: T) {
    var result: Boolean = false


    inline fun ifCondition(predicate: (T) -> Boolean, block: (T) -> Unit): KompanionConditionalChain<T> {
        if (!result && predicate(target)) {
            block(target)
            result = true
        }
        return this
    }

    inline fun elseCondition(block: (T) -> Unit): KompanionConditionalChain<T> {
        if (!result) {
            block(target)
        }
        return this
    }
}

/**
 *
 * Starts a conditional chain for an object.
 *
 *
 * ```Kt
 *
 * val num = 5
 *
 * num
 * .conditionalChain()
 * .ifCondition({ it > 10 }) { println("Greater than 10") }
 * .ifCondition({ it == 5 }) { println("Equal to 5") }
 * .elseCondition { println("None of the conditions met") }
 * ```
 */
fun <T> T.kompanionConditionalChain(): KompanionConditionalChain<T> = KompanionConditionalChain(this)

