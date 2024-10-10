package com.michael.kompanion.classes


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * Manages a transactional operation with rollback support.
 *
 * This class allows you to execute multiple operations in a transactional way. Each operation consists of an action and a rollback function.
 * If any action fails, it automatically rolls back all previously committed actions, executing their rollback functions in reverse order.
 *
 * Example usage:
 *
 * ```Kt
 *
 * val transactionManager = KompanionTransactionManager<String>()
 *
 * transactionManager.add(
 *     action = { "Task 1" },
 *     rollback = { println("Rollback Task 1: $it") }
 * )
 *
 * transactionManager.add(
 *     action = { "Task 2" },
 *     rollback = { println("Rollback Task 2: $it") }
 * )
 *
 * // Attempt to commit the tasks
 * try {
 *    transactionManager.commit() // Commits both tasks
 * } catch (e: Exception) {
 *    println("Transaction failed: ${e.message}")
 * }
 * ```
 *
 * When to use:
 * Use this class when you have multiple operations that need to be executed in a transactional way (all or none).
 * It is useful in scenarios where partial success is not acceptable, and rollback functionality is necessary for recovery.
 */
class KompanionTransactionManager<T> {
    // List of pairs, each containing an action and its corresponding rollback
    private val actions = mutableListOf<Pair<() -> T, (T) -> Unit>>()
    // List of successfully committed results
    private val committedResults = mutableListOf<T>()

    /**
     * Adds a new transactional action with its rollback.
     *
     * @param action The operation to execute in the transaction.
     * @param rollback The rollback action to execute if a transaction fails.
     */
    fun add(action: () -> T, rollback: (T) -> Unit) {
        actions.add(action to rollback)
    }

    /**
     * Commits all actions in the transaction.
     *
     * If any action fails, all previously committed actions are rolled back.
     *
     * @throws Exception If an action fails during execution.
     */
    fun commit() {
        try {
            for ((action, _) in actions) {
                committedResults.add(action())
            }
        } catch (e: Exception) {
            rollback()
            throw e
        }
    }

    /**
     * Rolls back all successfully committed actions in reverse order.
     */
    private fun rollback() {
        for ((_, rollback) in actions.reversed()) {
            if (committedResults.isNotEmpty()) {
                rollback(committedResults.removeLast())
            }
        }
    }
}
