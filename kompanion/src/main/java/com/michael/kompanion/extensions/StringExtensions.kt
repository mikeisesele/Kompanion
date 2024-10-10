package com.michael.kompanion.extensions

import java.util.Locale


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */


/**
 * Converts a string into title case (first letter capitalized, rest lowercased).
 * This is mainly due to [String.capitalize] being deprecated.
 *
 * Example usage:
 * ```Kt
 * println("hello world".kompanionTitleCase()) // Output: "Hello world"
 * ```
 */
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
 * Capitalizes the first letter of each word in a string.
 *
 * Example usage:
 * ```Kt
 * println("hello world".kompanionCapitalizeWords()) // Output: "Hello World"
 * ```
 */
fun String.kompanionCapitalizeWords(): String = split(" ").joinToString(" ") {
    it.replaceFirstChar { char ->
        if (char.isLowerCase()) char.titlecase(Locale.ROOT) else char.toString()
    }
}

/**
 * Checks if a string contains only digits.
 *
 * Example usage:
 * ```Kt
 * println("12345".kompanionIsNumeric()) // Output: true
 * println("123a5".kompanionIsNumeric()) // Output: false
 * ```
 */
fun String.kompanionIsNumeric(): Boolean = all { it.isDigit() }

/**
 * Checks if a string contains another string ignoring case.
 *
 * Example usage:
 * ```Kt
 * println("Hello".kompanionContainsIgnoreCase("hello")) // Output: true
 * ```
 */
fun String.kompanionContainsIgnoreCase(other: String): Boolean =
    this.lowercase(Locale.ROOT).contains(other.lowercase(Locale.ROOT))

/**
 * Truncates a string to the specified length and appends an ellipsis if necessary.
 *
 * Example usage:
 * ```Kt
 * println("This is a long sentence.".kompanionTruncate(10)) // Output: "This is a..."
 * ```
 */
fun String.kompanionTruncate(length: Int): String =
    if (this.length <= length) this else substring(0, length) + "..."

/**
 * Checks if a string is a valid email address using a regex pattern.
 *
 * Example usage:
 * ```Kt
 * println("test@example.com".kompanionIsValidEmail()) // Output: true
 * println("invalid-email".kompanionIsValidEmail())    // Output: false
 * ```
 */
fun String.kompanionIsValidEmail(): Boolean =
    matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex())

/**
 * Converts each word in a string to title case (capitalizing the first letter of each word).
 *
 * Example usage:
 * ```Kt
 * println("hello world".kompanionToTitleCase()) // Output: "Hello World"
 * ```
 */
fun String.kompanionToTitleCase(): String = split(" ").joinToString(" ") { it.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
} }

/**
 * Replaces multiple spaces in a string with a single space.
 *
 * Example usage:
 * ```Kt
 * println("This    is   spaced  out.".kompanionNormalizeSpaces()) // Output: "This is spaced out."
 * ```
 */
fun String.kompanionNormalizeSpaces(): String = replace("\\s+".toRegex(), " ")

/**
 * Finds the first non-repeating character in a string.
 *
 * Example usage:
 * ```Kt
 * println("swiss".kompanionFirstNonRepeatingChar()) // Output: 'w'
 * ```
 */
fun String.kompanionFirstNonRepeatingChar(): Char? =
    this.groupBy { it }.entries.firstOrNull { it.value.size == 1 }?.key

/**
 * Counts the number of vowels in a string (both uppercase and lowercase).
 *
 * Example usage:
 * ```Kt
 * println("hello world".kompanionCountVowels()) // Output: 3
 * ```
 */
fun String.kompanionCountVowels(): Int = count { it.lowercaseChar() in "aeiou" }

/**
 * Returns an empty string.
 */
fun emptyString() = ""

/**
 * Converts an empty string to `null`. If the string is blank, it returns `null`.
 *
 * Example usage:
 * ```Kt
 * println("".kompanionNullifyEmpty()) // Output: null
 * println("text".kompanionNullifyEmpty()) // Output: "text"
 * ```
 */
fun String.kompanionNullifyEmpty() = this.ifBlank { null }


/**
 * Returns the string or "Unknown Error" if the string is blank or `null`.
 *
 * Example usage:
 * ```Kt
 * println("".kompanionEmptyOrUnknownError()) // Output: "Unknown Error"
 * println("Some error".kompanionEmptyOrUnknownError()) // Output: "Some error"
 * ```
 */
fun String?.kompanionOrUnknownError() = this.orEmpty().ifBlank { "Unknown Error" }
