package com.michael.kompanion.utils

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.Locale
import kotlin.math.round
import kotlin.random.Random

/**
* Checks if all the provided objects are not null.
*
* @param objects List of objects to check.
* @return `true` if all objects are not null, `false` otherwise.
*/
fun <T> allNotNull(vararg objects: T?): Boolean {
    return objects.all { it != null }
}

/**
 * Checks if any of the provided objects is null.
 *
 * @param objects List of objects to check.
 * @return `true` if any object is null, `false` otherwise.
 */
fun <T> anyIsNull(vararg objects: T?): Boolean {
    return objects.any { it == null }
}

/**
 * Clears all elements in the provided arrays.
 * Note: This function expects the objects to be ArrayLists.
 *
 * @param objects List of ArrayLists to clear.
 */
fun <T> clearAllArray(vararg objects: T?) {
    objects.forEach { (it as ArrayList<*>).clear() }
}

/**
 * Extension function to set a default value if the object is null.
 *
 * @param defaultValue A lambda function providing the default value.
 * @return The original object if not null, otherwise the result of defaultValue().
 */
inline fun <T> T?.ifNullSetDefault(defaultValue: () -> T): T {
    return this ?: defaultValue()
}

/**
 * Extension function to perform an operation if the object is null.
 *
 * @param operation A lambda function representing the operation to perform if the object is null.
 */
inline fun <T> T?.ifNullPerform(operation: () -> Unit) {
    this ?: operation()
}


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
 * Reverses a string.
 */
fun String.reverse(): String = reversed()

/**
 * Capitalizes the first letter of each word in a string.
 */
fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

/**
 * Checks if a string contains only digits.
 */
fun String.isNumeric(): Boolean = all { it.isDigit() }

/**
 * Generates a random integer within the specified range.
 */
fun randomInt(min: Int, max: Int): Int = (min..max).random()

/**
 * Generates a random element from a list.
 */
fun <T> List<T>.randomElement(): T? = if (isEmpty()) null else get((indices).random())

/**
 * Repeats a string a specified number of times.
 */
fun String.repeat(times: Int): String = repeat(times)

/**
 * Creates a new list with the elements shuffled.
 */
fun <T> List<T>.shuffle(): List<T> = toMutableList().apply { shuffle() }

/**
 * Converts a string to a list of characters.
 */
fun String.toCharArray(): CharArray = toCharArray()

/**
 * Converts a string to a list of CharArrays.
 */
fun String.toCharArrayList(): List<CharArray> = toCharArray().toList()

fun String.toCharList(): List<Char> = this.toCharList()

/**
 * Splits a string into lines.
 */
fun String.splitLines(): List<String> = lines()

/**
 * Checks if a string contains another string ignoring case.
 */
fun String.containsIgnoreCase(other: String): Boolean = this.toLowerCase(Locale.ROOT).contains(other.toLowerCase(Locale.ROOT))

/**
 * Pads a string to the specified length with spaces.
 */
fun String.padSpaces(length: Int): String = padEnd(length)

/**
 * Truncates a string to the specified length and appends an ellipsis if necessary.
 */
fun String.truncate(length: Int): String = if (length >= length) this else substring(0, length) + "..."

/**
 * Checks if a string is empty or consists only of whitespace characters.
 */
fun String.isNullOrBlank(): Boolean = isNullOrBlank()

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
 * Checks if a string is a valid email address.
 */
fun String.isValidEmail(): Boolean = matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex())


/**
 * Creates a shallow copy of the list.
 */
fun <T> List<T>.shallowCopy(): List<T> = ArrayList(this)

/**
 * Creates a shallow copy of the set.
 */
fun <T> Set<T>.shallowCopy(): Set<T> = HashSet(this)

/**
 * Creates a shallow copy of the map.
 */
fun <K, V> Map<K, V>.shallowCopy(): Map<K, V> = HashMap(this)

/**
 * Calculates the factorial of a non-negative integer.
 */
fun factorial(n: Int): Long {
    require(n >= 0) { "Factorial is defined only for non-negative integers." }
    var result = 1L
    for (i in 2..n) {
        result *= i
    }
    return result
}

/**
 * Checks if a string is a palindrome.
 */
fun isPalindrome(str: String): Boolean = str == str.reversed()

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
 * Checks if a number is prime.
 */
fun isPrime(number: Long): Boolean {
    if (number <= 1) return false
    for (i in 2..(number / 2)) {
        if (number % i == 0L) {
            return false
        }
    }
    return true
}

/**
 * Calculates the greatest common divisor (GCD) of two numbers using Euclid's algorithm.
 */
fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

/**
 * Calculates the least common multiple (LCM) of two numbers.
 */
fun lcm(a: Int, b: Int): Int = (a * b) / gcd(a, b)

/**
 * Converts a string to title case (each word capitalized).
 */
fun String.toTitleCase(): String = split(" ").joinToString(" ") { it.capitalize() }

/**
 * Replaces multiple spaces in a string with a single space.
 */
fun String.normalizeSpaces(): String = replace("\\s+".toRegex(), " ")

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

/**
 * Checks if a number is even.
 */
fun Int.isEven(): Boolean = this % 2 == 0

/**
 * Checks if a number is odd.
 */
fun Int.isOdd(): Boolean = this % 2 != 0

/**
 * Finds the first non-repeating character in a string.
 */
fun String.firstNonRepeatingChar(): Char? = this.groupBy { it }.entries.firstOrNull { it.value.size == 1 }?.key

/**
 * Counts the number of vowels in a string.
 */
fun String.countVowels(): Int = count { it.toLowerCase() in "aeiou" }


fun emptyString() = ""

fun <T> T.toList(): List<T> {
    return listOf(this)
}

fun Int.random(): Int {
    return (1..this).random()
}

fun floatRandom(min: Float, max: Float): Float {
    val randomValue = Random.nextFloat() * (max - min) + min
    return round(randomValue * 10) / 10
}


fun <T> List<T>.randomItems(count: Int): List<T> {
    return if (count >= size) {
        this.shuffled() // If requested count is larger or equal to the list size, shuffle and return
    } else {
        this.shuffled().take(count) // Shuffle the list and return the first 'count' items
    }
}
