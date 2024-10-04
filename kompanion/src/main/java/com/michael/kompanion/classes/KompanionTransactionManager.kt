package com.michael.kompanion.classes


/**
 * Add operations to execute and handles them in a transactional way.
 * Manages a transactional operation with rollback support.
 *
 *
 * ```Kt
 *
 * val transactionManager = TransactionManager<String>()
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
 *    transactionManager.commit()
 * } catch (e: Exception) {
 *    println("Transaction failed: ${e.message}")
 * }
 *
 * ```
 *
 */
class KompanionTransactionManager<T> {
    private val actions = mutableListOf<Pair<() -> T, (T) -> Unit>>()
    private val committedResults = mutableListOf<T>()

    fun add(action: () -> T, rollback: (T) -> Unit) {
        actions.add(action to rollback)
    }

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

    private fun rollback() {
        for ((_, rollback) in actions.reversed()) {
            if (committedResults.isNotEmpty()) {
                rollback(committedResults.removeLast())
            }
        }
    }
}
