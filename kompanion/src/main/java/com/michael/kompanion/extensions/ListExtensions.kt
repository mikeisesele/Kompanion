package com.michael.kompanion.extensions

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * Reverses the elements of a mutable list in place.
 *
 * Example usage:
 * ```Kt
 * val list = mutableListOf(1, 2, 3, 4)
 * list.kompanionReverse()
 * println(list) // Output: [4, 3, 2, 1]
 * ```
 */
fun <T> MutableList<T>.kompanionReverse() {
    var left = 0
    var right = size - 1
    while (left < right) {
        val temp = this[left]
        this[left] = this[right]
        this[right] = temp
        left++
        right--
    }
}

/**
 * Checks if a list is sorted in ascending order.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3, 4)
 * println(list.kompanionIsSorted()) // Output: true
 * ```
 */
fun <T : Comparable<T>> List<T>.kompanionIsSorted(): Boolean {
    for (i in 0 until size - 1) {
        if (this[i] > this[i + 1]) {
            return false
        }
    }
    return true
}

/**
 * Checks if two lists contain the same elements regardless of order.
 *
 * Example usage:
 * ```Kt
 * val list1 = listOf(1, 2, 3)
 * val list2 = listOf(3, 1, 2)
 * println(list1.kompanionHasSameElementsAs(list2)) // Output: true
 * ```
 */
fun <T> List<T>.kompanionHasSameElementsAs(other: List<T>): Boolean {
    if (size != other.size) return false
    val copy = this.toMutableList()
    for (element in other) {
        if (!copy.remove(element)) {
            return false
        }
    }
    return copy.isEmpty()
}

/**
 * Returns the most common element in a list.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 2, 3, 3, 3)
 * println(list.kompanionMostCommonElement()) // Output: 3
 * ```
 */
fun <T> List<T>.kompanionMostCommonElement(): T? {
    if (isEmpty()) return null
    val elementCountMap = groupBy { it }.mapValues { it.value.size }
    val maxCount = elementCountMap.values.maxOrNull() ?: 0
    return elementCountMap.entries.firstOrNull { it.value == maxCount }?.key
}

/**
 * Checks if two lists contain the same elements in the same order.
 *
 * Example usage:
 * ```Kt
 * val list1 = listOf(1, 2, 3)
 * val list2 = listOf(1, 2, 3)
 * println(list1.kompanionContentEquals(list2)) // Output: true
 * ```
 */
fun <T> List<T>.kompanionContentEquals(other: List<T>): Boolean = this == other

/**
 * Removes duplicate elements from a list while preserving the order.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 2, 3, 4, 4)
 * println(list.kompanionDistinctByPreservingOrder()) // Output: [1, 2, 3, 4]
 * ```
 */
fun <T> List<T>.kompanionDistinctByPreservingOrder(): List<T> {
    val seen = mutableSetOf<T>()
    return filter { seen.add(it) }
}

/**
 * Splits a list into chunks of a specified size.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3, 4, 5)
 * println(list.kompanionChunked(2)) // Output: [[1, 2], [3, 4], [5]]
 * ```
 */
fun <T> List<T>.kompanionChunked(size: Int): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var index = 0
    while (index < this.size) {
        result.add(subList(index, minOf(index + size, this.size)))
        index += size
    }
    return result
}

/**
 * Transposes a 2D list (list of lists).
 *
 * Example usage:
 * ```Kt
 * val matrix = listOf(listOf(1, 2), listOf(3, 4))
 * println(matrix.kompanionTranspose()) // Output: [[1, 3], [2, 4]]
 * ```
 */
fun <T> List<List<T>>.kompanionTranspose(): List<List<T>> =
    if (isEmpty()) emptyList()
    else this[0].indices.map { col -> map { it[col] } }

/**
 * Zips two lists together into a list of pairs.
 *
 * Example usage:
 * ```Kt
 * val list1 = listOf(1, 2, 3)
 * val list2 = listOf("A", "B", "C")
 * println(list1.kompanionZipWith(list2)) // Output: [(1, "A"), (2, "B"), (3, "C")]
 * ```
 */
fun <T, U> List<T>.kompanionZipWith(other: List<U>): List<Pair<T, U>> {
    val size = minOf(size, other.size)
    val result = mutableListOf<Pair<T, U>>()
    for (i in 0 until size) {
        result.add(this[i] to other[i])
    }
    return result
}

/**
 * Performs a deep clone of a list of serializable objects.
 *
 * Example usage:
 * ```Kt
 * val list = listOf("A", "B", "C")
 * val clonedList = list.kompanionDeepClone()
 * println(clonedList) // Output: ["A", "B", "C"]
 * ```
 */
@Suppress("UNCHECKED_CAST")
fun <T> List<T>.kompanionDeepClone(): List<T> {
    val byteArrayOutputStream = ByteArrayOutputStream()
    val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
    objectOutputStream.writeObject(this)
    objectOutputStream.flush()
    objectOutputStream.close()
    val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
    val objectInputStream = ObjectInputStream(byteArrayInputStream)
    return objectInputStream.readObject() as List<T>
}

/**
 * Reverses the elements of an array in place.
 *
 * Example usage:
 * ```Kt
 * val array = arrayOf(1, 2, 3, 4)
 * array.kompanionReverse()
 * println(array.contentToString()) // Output: [4, 3, 2, 1]
 * ```
 */
fun <T> Array<T>.kompanionReverse() {
    var left = 0
    var right = size - 1
    while (left < right) {
        val temp = this[left]
        this[left] = this[right]
        this[right] = temp
        left++
        right--
    }
}

/**
 * Removes an item from a mutable list.
 *
 * Example usage:
 * ```Kt
 * val list = mutableListOf(1, 2, 3)
 * list.kompanionRemoveItem(2)
 * println(list) // Output: [1, 3]
 * ```
 */
fun <T> MutableList<T>.kompanionRemoveItem(item: T): Boolean {
    val index = indexOf(item)
    return if (index != -1) {
        removeAt(index)
        true
    } else false
}

/**
 * Checks if an array is sorted in ascending order.
 *
 * Example usage:
 * ```Kt
 * val array = arrayOf(1, 2, 3)
 * println(array.kompanionIsSorted()) // Output: true
 * ```
 */
fun <T : Comparable<T>> Array<T>.kompanionIsSorted(): Boolean {
    for (i in 0 until size - 1) {
        if (this[i] > this[i + 1]) {
            return false
        }
    }
    return true
}

/**
 * Checks if two arrays contain the same elements regardless of order.
 *
 * Example usage:
 * ```Kt
 * val array1 = arrayOf(1, 2, 3)
 * val array2 = arrayOf(3, 1, 2)
 * println(array1.kompanionHasSameElementsAs(array2)) // Output: true
 * ```
 */
fun <T> Array<T>.kompanionHasSameElementsAs(other: Array<T>): Boolean {
    if (size != other.size) return false
    val copy = this.toMutableList()
    for (element in other) {
        if (!copy.remove(element)) return false
    }
    return copy.isEmpty()
}

/**
 * Calculates the sum of all elements in a list of numbers.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3)
 * println(list.kompanionSumItems()) // Output: 6
 * ```
 */
fun List<Int>.kompanionSumItems(): Int = reduce { acc, num -> acc + num }

/**
 * Calculates the product of all elements in a list of numbers.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3)
 * println(list.kompanionProductOfItems()) // Output: 6
 * ```
 */
fun List<Int>.kompanionProductOfItems(): Long = map { it.toLong() }.reduce { acc, num -> acc * num }

/**
 * Generates a random element from a list.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3)
 * println(list.kompanionRandomElement()) // Output: A random element from the list
 * ```
 */
fun <T> List<T>.kompanionRandomElement(): T? = if (isEmpty()) null else get((indices).random())

/**
 * Creates a new list with the elements shuffled.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3)
 * println(list.kompanionShuffle()) // Output: A shuffled list, e.g., [2, 1, 3]
 * ```
 */
fun <T> List<T>.kompanionShuffle(): List<T> = toMutableList().apply { shuffle() }

/**
 * Converts a list of strings into a single string separated by the specified delimiter.
 *
 * Example usage:
 * ```Kt
 * val list = listOf("A", "B", "C")
 * println(list.kompanionJoinToString("-")) // Output: "A-B-C"
 * ```
 */
fun List<String>.kompanionJoinToString(delimiter: String): String = joinToString(delimiter)

/**
 * Finds the longest common prefix among a list of strings.
 *
 * Example usage:
 * ```Kt
 * val list = listOf("flower", "flow", "flight")
 * println(list.kompanionLongestCommonPrefix()) // Output: "fl"
 * ```
 */
fun List<String>.kompanionLongestCommonPrefix(): String {
    if (isEmpty()) return ""
    val shortest = minByOrNull { it.length } ?: return ""
    for (i in shortest.indices) {
        val char = shortest[i]
        if (none { it.length <= i || it[i] != char }) {
            return shortest.substring(0, i + 1)
        }
    }
    return shortest
}

/**
 * Finds the shortest common suffix among a list of strings.
 *
 * Example usage:
 * ```Kt
 * val list = listOf("action", "fraction", "reaction")
 * println(list.kompanionShortestCommonSuffix()) // Output: "ion"
 * ```
 */
fun List<String>.kompanionShortestCommonSuffix(): String {
    if (isEmpty()) return ""
    val longest = maxByOrNull { it.length } ?: return ""
    for (i in longest.indices.reversed()) {
        val char = longest[i]
        if (none { it.length <= i || it[it.length - 1 - i] != char }) {
            return longest.substring(longest.length - i - 1)
        }
    }
    return longest
}

/**
 * Creates a shallow copy of the list.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3)
 * val copy = list.kompanionShallowCopy()
 * println(copy) // Output: [1, 2, 3]
 * ```
 */
fun <T> List<T>.kompanionShallowCopy(): List<T> = ArrayList(this)

/**
 * Generates all permutations of a given list.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3)
 * println(list.kompanionPermutations()) // Output: All permutations of the list
 * ```
 */
fun <T> List<T>.kompanionPermutations(): List<List<T>> {
    if (isEmpty()) return listOf(emptyList())
    val result = mutableListOf<List<T>>()
    val first = first()
    val rest = drop(1)
    val permutations = rest.kompanionPermutations()
    for (perm in permutations) {
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList().apply { add(i, first) }
            result.add(newPerm)
        }
    }
    return result
}

/**
 * Calculates the median of a list of numbers.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 3, 2, 4)
 * println(list.kompanionMedian()) // Output: 2.5
 * ```
 */
fun List<Int>.kompanionMedian(): Double {
    val sortedList = sorted()
    val size = sortedList.size
    return if (size % 2 == 0) {
        (sortedList[size / 2 - 1] + sortedList[size / 2]) / 2.0
    } else {
        sortedList[size / 2].toDouble()
    }
}

/**
 * Checks if all elements in a list are unique.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3, 4)
 * println(list.kompanionAllUnique()) // Output: true
 * ```
 */
fun <T> List<T>.kompanionAllUnique(): Boolean = toSet().size == size

/**
 * Counts the occurrences of each element in a list.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 2, 3)
 * println(list.kompanionCountOccurrences()) // Output: {1=1, 2=2, 3=1}
 * ```
 */
fun <T> List<T>.kompanionCountOccurrences(): Map<T, Int> = groupBy { it }.mapValues { it.value.size }

/**
 * Finds the mode (most common element) in a list.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 2, 3)
 * println(list.mode()) // Output: 2
 * ```
 */
fun <T> List<T>.mode(): T? = kompanionCountOccurrences().maxByOrNull { it.value }?.key

/**
 * Converts an object to a list.
 *
 * Example usage:
 * ```Kt
 * val item = "Hello"
 * println(item.kompanionToList()) // Output: ["Hello"]
 * ```
 */
fun <T> T.kompanionToList(): List<T> {
    return listOf(this)
}

/**
 * Randomizes the elements of the list and returns a specified number of elements.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3, 4, 5)
 * println(list.kompanionRandomizeItems(3)) // Output: 3 randomly selected elements from the list
 * ```
 */
fun <T> List<T>.kompanionRandomizeItems(count: Int): List<T> {
    return if (count >= size) {
        this.shuffled() // If requested count is larger or equal to the list size, shuffle and return
    } else {
        this.shuffled().take(count) // Shuffle the list and return the first 'count' items
    }
}

/**
 * Returns the list or an empty list if it's null.
 *
 * Example usage:
 * ```Kt
 * val list: List<Int>? = null
 * println(list.kompanionRrEmptyList()) // Output: []
 * ```
 */
fun <T> List<T>?.kompanionRrEmptyList(): List<T> {
    return this ?: emptyList()
}

/**
 * Converts a list of pairs to a map.
 *
 * Example usage:
 * ```Kt
 * val list = listOf("A" to 1, "B" to 2)
 * println(list.kompanionToMap()) // Output: {A=1, B=2}
 * ```
 */
fun <K, V> List<Pair<K, V>>.kompanionToMap(): Map<K, V> {
    return this.associate { it.first to it.second }
}

/**
 * Finds the element with the most occurrences in the collection.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 2, 3)
 * println(list.kompanionFindMostFrequent()) // Output: 2
 * ```
 */
fun <T> Collection<T>.kompanionFindMostFrequent(): T? {
    return groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
}

/**
 * Executes a block if all elements in the list are unique.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3)
 * list.kompanionIfAllUnique { println("All elements are unique!") }
 * // Output: "All elements are unique!"
 * ```
 */
inline fun <T> List<T>.kompanionIfAllUnique(block: () -> Unit) {
    if (size == toSet().size) {
        block()
    }
}

/**
 * Runs a block of code on every nth element in the collection.
 *
 * Example usage:
 * ```Kt
 * val list = listOf(1, 2, 3, 4, 5)
 * list.kompanionForEveryNth(2) { println(it) }
 * // Output: 1, 3, 5
 * ```
 */
inline fun <T> List<T>.kompanionForEveryNth(n: Int, block: (T) -> Unit) {
    for (i in indices step n) {
        block(this[i])
    }
}
