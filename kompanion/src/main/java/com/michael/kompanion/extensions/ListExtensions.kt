package com.michael.kompanion.extensions

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


/**
 * Reverses the elements of a list in place.
 */
fun <T> MutableList<T>.reverse() {
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
 */
fun <T : Comparable<T>> List<T>.isSorted(): Boolean {
    for (i in 0 until size - 1) {
        if (this[i] > this[i + 1]) {
            return false
        }
    }
    return true
}

/**
 * Checks if two lists contain the same elements regardless of order.
 */
fun <T> List<T>.hasSameElementsAs(other: List<T>): Boolean {
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
 */
fun <T> List<T>.mostCommonElement(): T? {
    if (isEmpty()) return null
    val elementCountMap = groupBy { it }.mapValues { it.value.size }
    val maxCount = elementCountMap.values.maxOrNull() ?: 0
    return elementCountMap.entries.firstOrNull { it.value == maxCount }?.key
}

/**
 * Checks if two lists contain the same elements in the same order.
 */
fun <T> List<T>.contentEquals(other: List<T>): Boolean = this == other

/**
 * Removes duplicate elements from a list while preserving the order.
 */
fun <T> List<T>.distinctByPreservingOrder(): List<T> {
    val seen = mutableSetOf<T>()
    return filter { seen.add(it) }
}

/**
 * Splits a list into chunks of a specified size.
 */
fun <T> List<T>.chunked(size: Int): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var index = 0
    while (index < size) {
        result.add(subList(index, minOf(index + size, size)))
        index += size
    }
    return result
}


/**
 * Transposes a 2D list (list of lists).
 */
fun <T> List<List<T>>.transpose(): List<List<T>> =
    if (isEmpty()) emptyList()
    else this[0].indices.map { col -> map { it[col] } }

/**
 * Zips two lists together into a list of pairs.
 */
fun <T, U> List<T>.zipWith(other: List<U>): List<Pair<T, U>> {
    val size = minOf(size, other.size)
    val result = mutableListOf<Pair<T, U>>()
    for (i in 0 until size) {
        result.add(this[i] to other[i])
    }
    return result
}

/**
 * Performs a deep clone of a list of serializable objects.
 */
@Suppress("UNCHECKED_CAST")
fun <T> List<T>.deepClone(): List<T> {
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
 */
fun <T> Array<T>.reverse() {
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
 * remove an item from a mutable list
 */
fun <T> MutableList<T>.removeItem(item: T): Boolean {
    val index = indexOf(item)
    return if (index != -1) {
        removeAt(index)
        true
    }  else false
}


/**
 * Checks if an array is sorted in ascending order.
 */
fun <T : Comparable<T>> Array<T>.isSorted(): Boolean {
    for (i in 0 until size - 1) {
        if (this[i] > this[i + 1]) {
            return false
        }
    }
    return true
}

/**
 * Checks if two arrays contain the same elements regardless of order.
 */
fun <T> Array<T>.hasSameElementsAs(other: Array<T>): Boolean {
    if (size != other.size) return false
    val copy = this.toMutableList()
    for (element in other) {
        if (!copy.remove(element)) { return false }
    }
    return copy.isEmpty()
}


/**
 * Calculates the sum of all elements in a list of numbers.
 */
fun List<Int>.sum(): Int = reduce { acc, num -> acc + num }

/**
 * Calculates the product of all elements in a list of numbers.
 */
fun List<Int>.product(): Long = map { it.toLong() }.reduce { acc, num -> acc * num }

/**
 * Generates a random element from a list.
 */
fun <T> List<T>.randomElement(): T? = if (isEmpty()) null else get((indices).random())

/**
 * Creates a new list with the elements shuffled.
 */
fun <T> List<T>.shuffle(): List<T> = toMutableList().apply { shuffle() }


/**
 * Converts a list of strings into a single string separated by the specified delimiter.
 */
fun List<String>.joinToString(delimiter: String): String = joinToString(delimiter)

/**
 * Finds the longest common prefix among a list of strings.
 */
fun List<String>.longestCommonPrefix(): String {
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
 */
fun List<String>.shortestCommonSuffix(): String {
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
 */
fun <T> List<T>.shallowCopy(): List<T> = ArrayList(this)

/**
 * Generates all permutations of a given list.
 */
fun <T> List<T>.permutations(): List<List<T>> {
    if (isEmpty()) return listOf(emptyList())
    val result = mutableListOf<List<T>>()
    val first = first()
    val rest = drop(1)
    val permutations = rest.permutations()
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
 */
fun List<Int>.median(): Double {
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
 */
fun <T> List<T>.allUnique(): Boolean = toSet().size == size

/**
 * Counts the occurrences of each element in a list.
 */
fun <T> List<T>.countOccurrences(): Map<T, Int> = groupBy { it }.mapValues { it.value.size }

/**
 * Finds the mode (most common element) in a list.
 */
fun <T> List<T>.mode(): T? = countOccurrences().maxByOrNull { it.value }?.key

fun <T> T.toList(): List<T> {
    return listOf(this)
}


fun <T> List<T>.randomItems(count: Int): List<T> {
    return if (count >= size) {
        this.shuffled() // If requested count is larger or equal to the list size, shuffle and return
    } else {
        this.shuffled().take(count) // Shuffle the list and return the first 'count' items
    }
}

/**
 * Returns the list or an empty list if it's null.
 */
fun <T> List<T>?.orEmptyList(): List<T> {
    return this ?: emptyList()
}

/**
 * Flattens a list of lists into a single list.
 */
fun <T> List<List<T>>.flatten(): List<T> {
    return this.flatMap { it }
}

/**
 * Converts a list of pairs to a map.
 */
fun <K, V> List<Pair<K, V>>.toMap(): Map<K, V> {
    return this.associate { it.first to it.second }
}



/**
 * Finds the element with the most occurrences in the collection.
 */
fun <T> Collection<T>.findMostFrequent(): T? {
    return groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
}


/**
 * Executes a block if all elements in the list are unique.
 */
inline fun <T> List<T>.ifAllUnique(block: () -> Unit) {
    if (size == toSet().size) {
        block()
    }
}

/**
 * Runs a block of code on every nth element in the collection.
 */
inline fun <T> List<T>.forEveryNth(n: Int, block: (T) -> Unit) {
    for (i in indices step n) {
        block(this[i])
    }
}