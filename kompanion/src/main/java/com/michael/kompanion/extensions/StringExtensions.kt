package com.michael.kompanion.extensions

import java.util.Locale

/**
 * Simple utility that converts a string into title case.
 * This is mainly due to [String.capitalize] being deprecated.
 * */
fun String.kompanionTitleCase(): String {
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
fun String.kompanionReverse(): String = reversed()

/**
 * Capitalizes the first letter of each word in a string.
 */
fun String.kompanionCapitalizeWords(): String = split(" ").joinToString(" ") {
    it.replaceFirstChar {char ->
        if (char.isLowerCase()) char.titlecase(
            Locale.ROOT
        ) else char.toString()
    }
}

/**
 * Checks if a string contains only digits.
 */
fun String.kompanionIsNumeric(): Boolean = all { it.isDigit() }

/**
 * Repeats a string a specified number of times.
 */
fun String.kompanionRepeat(times: Int): String = repeat(times)


/**
 * Converts a string to a list of characters.
 */
fun String.kompanionToCharArray(): CharArray = toCharArray()

fun String.kompanionToCharList(): List<Char> = this.kompanionToCharList()

/**
 * Splits a string into lines.
 */
fun String.kompanionSplitLines(): List<String> = lines()

/**
 * Checks if a string contains another string ignoring case.
 */
fun String.kompanionContainsIgnoreCase(other: String): Boolean =
    this.lowercase(Locale.ROOT).contains(other.lowercase(Locale.ROOT))
/**
 * Pads a string to the specified length with spaces.
 */
fun String.kompanionPadSpaces(length: Int): String = padEnd(length)

/**
 * Truncates a string to the specified length and appends an ellipsis if necessary.
 */
fun String.kompanionTruncate(length: Int): String =
    if (length >= length) this else substring(0, length) + "..."

/**
 * Checks if a string is empty or consists only of whitespace characters.
 */
fun String.kompanionIsNullOrBlank(): Boolean = isNullOrBlank()

/**
 * Checks if a string is a valid email address.
 */
fun String.kompanionIsValidEmail(): Boolean =
    matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex())

/**
 * Converts a string to title case (each word capitalized).
 */
fun String.kompanionToTitleCase(): String = split(" ").joinToString(" ") { it.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        Locale.ROOT
    ) else it.toString()
} }

/**
 * Replaces multiple spaces in a string with a single space.
 */
fun String.kompanionNormalizeSpaces(): String = replace("\\s+".toRegex(), " ")

/**
 * Finds the first non-repeating character in a string.
 */
fun String.kompanionFirstNonRepeatingChar(): Char? =
    this.groupBy { it }.entries.firstOrNull { it.value.size == 1 }?.key

/**
 * Counts the number of vowels in a string.
 */
fun String.kompanionCountVowels(): Int = count { it.lowercaseChar() in "aeiou" }


fun kompanionEmptyString() = ""

fun String.kompanionNullifyEmpty() = this.ifBlank { null }
