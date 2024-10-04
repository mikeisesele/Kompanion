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
 * ```Kt
 *
 * var list = KompanionTrackedImmutableList(listOf(1, 2, 3))
 *
 * list = list.add(4)
 *
 * list = list.remove(2)
 *
 * println(list.history) // Output: ["Added 4", "Removed 2"]
 * ```
 */
class KompanionTrackedImmutableList<T>(
    private val initialList: List<T> = emptyList()
) {
    private val changes = mutableListOf<String>()
    private var list = initialList.toList()

    val history: List<String> get() = changes.toList()

    fun add(element: T): KompanionTrackedImmutableList<T> {
        changes.add("Added $element")
        return KompanionTrackedImmutableList(list + element)
    }

    fun remove(element: T): KompanionTrackedImmutableList<T> {
        changes.add("Removed $element")
        return KompanionTrackedImmutableList(list - element)
    }
}
