package com.michael.kompanion.extensions

import java.util.Locale

/**
 * Simple utility that converts a string into title case.
 * This is mainly due to [String.capitalize] being deprecated.
 * */
fun String.titleCase(): String {
    val src = this
    return buildString {
        if (src.isNotEmpty()) {
            append(src[0].uppercase(Locale.getDefault()))
        }
        if (src.length > 1) {
            append(src.substring(1).lowercase(Locale.getDefault()))
        }
    }
}


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
 * Repeats a string a specified number of times.
 */
fun String.repeat(times: Int): String = repeat(times)


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
 * Checks if a string is a valid email address.
 */
fun String.isValidEmail(): Boolean = matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex())

/**
 * Converts a string to title case (each word capitalized).
 */
fun String.toTitleCase(): String = split(" ").joinToString(" ") { it.capitalize() }

/**
 * Replaces multiple spaces in a string with a single space.
 */
fun String.normalizeSpaces(): String = replace("\\s+".toRegex(), " ")

/**
 * Finds the first non-repeating character in a string.
 */
fun String.firstNonRepeatingChar(): Char? = this.groupBy { it }.entries.firstOrNull { it.value.size == 1 }?.key

/**
 * Counts the number of vowels in a string.
 */
fun String.countVowels(): Int = count { it.toLowerCase() in "aeiou" }


fun emptyString() = ""

