package com.michael.kompanion.classes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Chains multiple conditional actions for each emission in a Flow.
 */
class KompanionFlowConditionalChain<T>(private val flow: Flow<T>) {

    private var result: Boolean = false
    private val conditions: MutableList<Pair<(T) -> Boolean, (T) -> Unit>> = mutableListOf()
    private var elseBlock: ((T) -> Unit)? = null

    /**
     * Applies the given block if the predicate is true for the emitted item.
     *
     * @param predicate A condition to check against the emitted item.
     * @param block The action to execute if the condition is met.
     * @return The current chain for further condition checks.
     */
    fun ifCondition(predicate: (T) -> Boolean, block: (T) -> Unit): KompanionFlowConditionalChain<T> {
        conditions.add(predicate to block)
        return this
    }

    /**
     * Executes the provided block if no previous condition was met for the emitted item.
     *
     * @param block The action to execute if no conditions were met.
     * @return The current chain for further condition checks.
     */
    fun elseCondition(block: (T) -> Unit): KompanionFlowConditionalChain<T> {
        this.elseBlock = block
        return this
    }

    /**
     * Collects the flow, applying the conditional logic.
     */
    fun collect() = flow {
        flow.collect { item ->
            result = false
            for ((predicate, action) in conditions) {
                if (!result && predicate(item)) {
                    action(item)
                    result = true
                    break
                }
            }
            if (!result) {
                elseBlock?.invoke(item)
            }
            emit(item) // Emit the item after processing
        }
    }
}

/**
 * Extension function that allows for a conditional chain on each emission of a Flow.
 *
 * Example usage:
 *
 * ```Kt
 *
 * flowOf(5, 10, 15)
 *     .kompanionFlowConditionalChain()
 *     .ifCondition({ it > 10 }) { println("Greater than 10") }
 *     .ifCondition({ it == 5 }) { println("Equal to 5") }
 *     .elseCondition { println("None of the conditions met") }
 *     .collect()
 * ```
 *
 * Output:
 * Equal to 5
 * None of the conditions met
 * Greater than 10
 *
 * When to use:
 * This method is useful when you want to set up a conditional chain for each emission in a flow.
 *
 * @return A FlowConditionalChain initialized for the flow.
 */
fun <T> Flow<T>.kompanionFlowConditionalChain(): KompanionFlowConditionalChain<T> = KompanionFlowConditionalChain(this)
