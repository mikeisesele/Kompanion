package com.michael.kompanion.classes

/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * Immutable list with change tracking.
 *
 * This class represents an immutable list that keeps track of the changes (additions and removals) made to the list.
 * Each time an element is added or removed, a new instance of the list is returned (due to immutability),
 * and a history of the changes is maintained.
 *
 * Example usage:
 *
 * ```Kt
 *
 * var list = KompanionTrackedImmutableList(listOf(1, 2, 3))
 *
 * list = list.add(4) // Adds 4 to the list, returns a new instance
 *
 * list = list.remove(2) // Removes 2 from the list, returns a new instance
 *
 * println(list.history) // Output: ["Added 4", "Removed 2"]
 * ```
 *
 * When to use:
 * Use this class when you need an immutable list that tracks the history of changes over time.
 * It's useful in scenarios where immutability is desired, and keeping track of the modifications (like undo/redo functionality, debugging, or audit logging) is important.
 *
 * @param initialList The initial immutable list.
 */
class KompanionTrackedImmutableList<T>(
    private val initialList: List<T> = emptyList()
) {
    private val changes = mutableListOf<String>()
    private var list = initialList.toList()

    /** The list of changes (history) made to the immutable list. */
    val history: List<String> get() = changes.toList()

    /**
     * Adds an element and returns a new instance of the list.
     */
    fun add(element: T): KompanionTrackedImmutableList<T> {
        changes.add("Added $element")
        return KompanionTrackedImmutableList(list + element).also { it.changes.addAll(this.changes) }
    }

    /**
     * Removes an element and returns a new instance of the list.
     */
    fun remove(element: T): KompanionTrackedImmutableList<T> {
        changes.add("Removed $element")
        return KompanionTrackedImmutableList(list - element).also { it.changes.addAll(this.changes) }
    }
}
