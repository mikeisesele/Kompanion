package com.michael.kompanion.extensions

import java.util.Locale
import kotlin.math.pow

/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */


/**
 * Generates a random number from the integer to 1,000,000,000.
 *
 * Example usage:
 * ```Kt
 * val random = 50.kompanionRandomFrom()
 * println(random) // Output: Random number between 100 and 1,000,000,000
 * ```
 */
fun Int.kompanionRandomFrom(): Int {
    return (this + 100..1000000000).random()
}

/**
 * Checks if a number is even.
 *
 * Example usage:
 * ```Kt
 * println(4.kompanionIsEven()) // Output: true
 * ```
 */
fun Int.kompanionIsEven(): Boolean = this % 2 == 0

/**
 * Checks if a number is odd.
 *
 * Example usage:
 * ```Kt
 * println(5.kompanionIsOdd()) // Output: true
 * ```
 */
fun Int.kompanionIsOdd(): Boolean = this % 2 != 0

/**
 * Generates a random number from the specified start to the current number.
 *
 * Example usage:
 * ```Kt
 * println(100.kompanionRandom(10)) // Output: Random number between 10 and 100
 * ```
 */
fun Int.kompanionRandom(start: Int = 1): Int {
    return (start..this).random()
}

/**
 * Checks if a number is prime.
 *
 * Example usage:
 * ```Kt
 * println(13.kompanionIsPrime()) // Output: true
 * ```
 */
fun Int.kompanionIsPrime(): Boolean {
    if (this <= 1) return false
    for (i in 2..(this / 2)) {
        if (this % i == 0) {
            return false
        }
    }
    return true
}

/**
 * Performs an action if the number is within the given range.
 *
 * Example usage:
 * ```Kt
 * 10.kompanionIfInRange(1..20) { println("$it is in range") }
 * // Output: "10 is in range"
 * ```
 */
inline fun Int.kompanionIfInRange(range: IntRange, action: (Int) -> Unit) {
    if (this in range) {
        action(this)
    }
}

/**
 * Returns the factorial of the number.
 *
 * Example usage:
 * ```Kt
 * println(5.kompanionFactorial()) // Output: 120
 * ```
 */
fun Int.kompanionFactorial(): Long {
    if (this < 0) return 0
    return (1..this).fold(1L) { acc, i -> acc * i }
}

/**
 * Returns the square of the number.
 *
 * Example usage:
 * ```Kt
 * println(4.kompanionSquared()) // Output: 16
 * ```
 */
fun Int.kompanionSquared(): Int = this * this

/**
 * Returns the cube of the number.
 *
 * Example usage:
 * ```Kt
 * println(3.kompanionCubed()) // Output: 27
 * ```
 */
fun Int.kompanionCubed(): Int = this * this * this

/**
 * Converts the number from degrees to radians.
 *
 * Example usage:
 * ```Kt
 * println(180.kompanionToRadians()) // Output: 3.14159 (approximately)
 * ```
 */
fun Int.kompanionToRadians(): Double = Math.toRadians(this.toDouble())

/**
 * Converts the number from radians to degrees.
 *
 * Example usage:
 * ```Kt
 * println(1.kompanionToDegrees()) // Output: 57.2958 (approximately)
 * ```
 */
fun Int.kompanionToDegrees(): Double = Math.toDegrees(this.toDouble())

/**
 * Returns the greatest common divisor (GCD) of this number and the given number.
 *
 * Example usage:
 * ```Kt
 * println(24.gcd(18)) // Output: 6
 * ```
 */
fun Int.gcd(other: Int): Int {
    var a = this
    var b = other
    while (b != 0) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}

/**
 * Returns the least common multiple (LCM) of this number and the given number.
 *
 * Example usage:
 * ```Kt
 * println(12.kompanionLCM(15)) // Output: 60
 * ```
 */
fun Int.kompanionLCM(other: Int): Int = (this * other) / this.gcd(other)

/**
 * Returns true if the number is a power of two.
 *
 * Example usage:
 * ```Kt
 * println(8.kompanionIsPowerOfTwo()) // Output: true
 * ```
 */
fun Int.kompanionIsPowerOfTwo(): Boolean = this > 0 && (this and (this - 1)) == 0

/**
 * Clamps the number within the provided range.
 *
 * Example usage:
 * ```Kt
 * println(50.kompanionClamp(10, 40)) // Output: 40
 * ```
 */
fun Int.kompanionClamp(min: Int, max: Int): Int = when {
    this < min -> min
    this > max -> max
    else -> this
}

/**
 * Returns the absolute value of the number.
 *
 * Example usage:
 * ```Kt
 * println((-10).kompanionAbsoluteValue()) // Output: 10
 * ```
 */
fun Int.kompanionAbsoluteValue(): Int = kotlin.math.abs(this)

/**
 * Returns the number raised to the power of [exp].
 *
 * Example usage:
 * ```Kt
 * println(2.kompanionPOW(3)) // Output: 8
 * ```
 */
fun Int.kompanionPOW(exp: Int): Long {
    if (exp < 0) throw IllegalArgumentException("Exponent must be non-negative")
    return (1..exp).fold(1L) { acc, _ -> acc * this }
}

/**
 * Performs an action a specified number of times.
 *
 * Example usage:
 * ```Kt
 * 3.kompanionRepeat { println("Hello!") }
 * // Output: "Hello!" printed 3 times
 * ```
 */
inline fun Int.kompanionRepeat(action: () -> Unit) {
    for (i in 1..this) action()
}

/**
 * Returns the percentage value of the number relative to [total].
 *
 * Example usage:
 * ```Kt
 * println(25.kompanionPercentOf(100)) // Output: 25.0
 * ```
 */
fun Int.kompanionPercentOf(total: Int): Double = if (total != 0) (this.toDouble() / total) * 100 else 0.0

/**
 * Returns the number with a specific percentage applied.
 *
 * Example usage:
 * ```Kt
 * println(200.kompanionApplyPercentage(15.0)) // Output: 30.0
 * ```
 */
fun Int.kompanionApplyPercentage(percentage: Double): Double = this * (percentage / 100)

/**
 * Returns the integer nearest to this number's square root.
 *
 * Example usage:
 * ```Kt
 * println(17.kompanionNearestSqrt()) // Output: 4
 * ```
 */
fun Int.kompanionNearestSqrt(): Int = kotlin.math.sqrt(this.toDouble()).toInt()

/**
 * Returns the number as a binary string.
 *
 * Example usage:
 * ```Kt
 * println(10.kompanionToBinaryString()) // Output: "1010"
 * ```
 */
fun Int.kompanionToBinaryString(): String = Integer.toBinaryString(this)

/**
 * Returns the number as a hexadecimal string.
 *
 * Example usage:
 * ```Kt
 * println(255.kompanionToHexString()) // Output: "ff"
 * ```
 */
fun Int.kompanionToHexString(): String = Integer.toHexString(this)

/**
 * Checks if the number is a palindrome.
 *
 * Example usage:
 * ```Kt
 * println(121.kompanionIsPalindrome()) // Output: true
 * ```
 */
fun Int.kompanionIsPalindrome(): Boolean {
    val original = this.toString()
    return original == original.reversed()
}

/**
 * Returns the reverse of the number.
 *
 * Example usage:
 * ```Kt
 * println(123.kompanionReverse()) // Output: 321
 * ```
 */
fun Int.kompanionReverse(): Int = this.toString().reversed().toInt()

/**
 * Converts the number to Roman numerals (valid for numbers 1-3999).
 *
 * Example usage:
 * ```Kt
 * println(1987.kompanionToRomanNumerals()) // Output: "MCMLXXXVII"
 * ```
 */
fun Int.kompanionToRomanNumerals(): String {
    if (this <= 0 || this > 3999) throw IllegalArgumentException("Number out of range (must be between 1 and 3999)")
    val numerals = listOf(
        1000 to "M", 900 to "CM", 500 to "D", 400 to "CD",
        100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
        10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I"
    )
    var num = this
    val roman = StringBuilder()
    for ((value, symbol) in numerals) {
        while (num >= value) {
            roman.append(symbol)
            num -= value
        }
    }
    return roman.toString()
}

/**
 * Returns true if the number is a perfect square.
 *
 * Example usage:
 * ```Kt
 * println(16.kompanionIsPerfectSquare()) // Output: true
 * ```
 */
fun Int.kompanionIsPerfectSquare(): Boolean =
    kotlin.math.sqrt(this.toDouble()).let { it == it.toInt().toDouble() }

/**
 * Returns true if the number is a perfect cube.
 *
 * Example usage:
 * ```Kt
 * println(27.kompanionIsPerfectCube()) // Output: true
 * ```
 */
fun Int.kompanionIsPerfectCube(): Boolean =
    this.toDouble().pow(1.0 / 3.0).let { it == it.toInt().toDouble() }

/**
 * Converts the number of seconds into a human-readable time format (HH:mm:ss).
 *
 * Example usage:
 * ```Kt
 * println(3661.kompanionToTimeFormat()) // Output: "01:01:01"
 * ```
 */
fun Int.kompanionToTimeFormat(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
}

/**
 * Rounds the number to the nearest multiple of [multiplier].
 *
 * Example usage:
 * ```Kt
 * println(47.kompanionRoundToNearest(5)) // Output: 45
 * ```
 */
fun Int.kompanionRoundToNearest(multiplier: Int): Int = ((this + multiplier / 2) / multiplier) * multiplier

/**
 * Returns the sum of digits of the number.
 *
 * Example usage:
 * ```Kt
 * println(123.kompanionSumOfDigits()) // Output: 6
 * ```
 */
fun Int.kompanionSumOfDigits(): Int = this.toString().map { it.toString().toInt() }.sum()

/**
 * Converts the number of days into a readable duration (years, months, days).
 *
 * Example usage:
 * ```Kt
 * println(800.kompanionToReadableDuration()) // Output: "2 years 2 months 10 days"
 * ```
 */
fun Int.kompanionToReadableDuration(): String {
    val years = this / 365
    val months = (this % 365) / 30
    val days = (this % 365) % 30
    return buildString {
        if (years > 0) append("$years years ")
        if (months > 0) append("$months months ")
        if (days > 0) append("$days days")
    }.trim()
}

/**
 * Returns the number as a formatted string with commas (e.g., 1,000,000).
 *
 * Example usage:
 * ```Kt
 * println(1000000.kompanionToFormattedString()) // Output: "1,000,000"
 * ```
 */
fun Int.kompanionToFormattedString(): String = "%,d".format(this)

/**
 * Checks if the number is divisible by all provided divisors.
 *
 * Example usage:
 * ```Kt
 * println(30.kompanionIsDivisibleByAll(2, 3, 5)) // Output: true
 * ```
 */
fun Int.kompanionIsDivisibleByAll(vararg divisors: Int): Boolean = divisors.all { this % it == 0 }



/**
 * Checks if the number is divisible by either [a] or [b].
 *
 * Example usage:
 * ```Kt
 * println(10.kompanionIsDivisibleByEither(2, 3)) // Output: true
 * println(7.kompanionIsDivisibleByEither(2, 3))  // Output: false
 * ```
 *
 * @param a First divisor.
 * @param b Second divisor.
 * @return `true` if the number is divisible by either [a] or [b], `false` otherwise.
 */
fun Int.kompanionIsDivisibleByEither(a: Int, b: Int): Boolean = this % a == 0 || this % b == 0

/**
 * Converts the number to a string with a specified number of leading zeros.
 *
 * Example usage:
 * ```Kt
 * println(5.kompanionPadWithZeros(3)) // Output: "005"
 * println(123.kompanionPadWithZeros(5)) // Output: "00123"
 * ```
 *
 * @param length The desired total length of the resulting string.
 * @return A string representation of the number padded with leading zeros to reach the specified [length].
 */
fun Int.kompanionPadWithZeros(length: Int): String = this.toString().padStart(length, '0')

/**
 * Returns the harmonic mean of this number and another.
 *
 * Example usage:
 * ```Kt
 * println(4.kompanionHarmonicMean(6)) // Output: 4.8
 * ```
 *
 * @param other The other integer to calculate the harmonic mean with.
 * @return The harmonic mean as a `Double`.
 */
fun Int.kompanionHarmonicMean(other: Int): Double = 2.0 * this * other / (this + other)

/**
 * Returns the median of the number and two other integers.
 *
 * Example usage:
 * ```Kt
 * println(10.kompanionMedianOf(20, 30)) // Output: 20
 * println(5.kompanionMedianOf(3, 8))    // Output: 5
 * ```
 *
 * @param other1 The first other integer.
 * @param other2 The second other integer.
 * @return The median value as an `Int`.
 */
fun Int.kompanionMedianOf(other1: Int, other2: Int): Int = listOf(this, other1, other2).sorted()[1]

/**
 * Calculates the logarithm of the number to a given base.
 *
 * Example usage:
 * ```Kt
 * println(8.kompanionLogBase(2)) // Output: 3.0
 * ```
 *
 * @param base The base for the logarithm.
 * @return The logarithm of the number to the specified [base] as a `Double`.
 */
fun Int.kompanionLogBase(base: Int): Double = kotlin.math.log(this.toDouble(), base.toDouble())

/**
 * Returns the average of the number and another integer.
 *
 * Example usage:
 * ```Kt
 * println(10.kompanionAverage(20)) // Output: 15.0
 * ```
 *
 * @param other The other integer to calculate the average with.
 * @return The average as a `Double`.
 */
fun Int.kompanionAverage(other: Int): Double = (this + other) / 2.0

/**
 * Checks if the number is a triangular number.
 * A triangular number is one that can form an equilateral triangle.
 *
 * Example usage:
 * ```Kt
 * println(6.kompanionIsTriangular())  // Output: true
 * println(7.kompanionIsTriangular())  // Output: false
 * ```
 *
 * @return `true` if the number is a triangular number, `false` otherwise.
 */
fun Int.kompanionIsTriangular(): Boolean {
    if (this <= 0) return false
    val n = (-1 + kotlin.math.sqrt(1.0 + 8 * this)).toInt()
    return n * (n + 1) / 2 == this
}

/**
 * Returns the nth triangular number.
 *
 * Example usage:
 * ```Kt
 * println(5.kompanionNthTriangular()) // Output: 15
 * ```
 *
 * @return The nth triangular number as an `Int`.
 */
fun Int.kompanionNthTriangular(): Int = this * (this + 1) / 2

/**
 * Checks if the number is an abundant number.
 * An abundant number is a number for which the sum of its proper divisors is greater than the number itself.
 *
 * Example usage:
 * ```Kt
 * println(12.kompanionIsAbundant()) // Output: true
 * println(10.kompanionIsAbundant()) // Output: false
 * ```
 *
 * @return `true` if the number is abundant, `false` otherwise.
 */
fun Int.kompanionIsAbundant(): Boolean = (1 until this).filter { this % it == 0 }.sum() > this

/**
 * Checks if the number is a deficient number.
 * A deficient number is a number for which the sum of its proper divisors is less than the number itself.
 *
 * Example usage:
 * ```Kt
 * println(8.kompanionIsDeficient()) // Output: true
 * println(12.kompanionIsDeficient()) // Output: false
 * ```
 *
 * @return `true` if the number is deficient, `false` otherwise.
 */
fun Int.kompanionIsDeficient(): Boolean = (1 until this).filter { this % it == 0 }.sum() < this

/**
 * Checks if the number is a happy number.
 * A happy number is defined by repeatedly replacing the number with the sum of the squares of its digits
 * until the number becomes 1 (happy) or falls into a loop (unhappy).
 *
 * Example usage:
 * ```Kt
 * println(19.kompanionIsHappy()) // Output: true
 * println(20.kompanionIsHappy()) // Output: false
 * ```
 *
 * @return `true` if the number is happy, `false` otherwise.
 */
fun Int.kompanionIsHappy(): Boolean {
    var num = this
    val seen = mutableSetOf<Int>()
    while (num != 1 && !seen.contains(num)) {
        seen.add(num)
        num = num.toString().map { it.toString().toInt() }.sumOf { it * it }
    }
    return num == 1
}

/**
 * Returns true if the number is a power of three.
 *
 * Example usage:
 * ```Kt
 * println(27.kompanionIsPowerOfThree()) // Output: true
 * println(28.kompanionIsPowerOfThree()) // Output: false
 * ```
 *
 * @return `true` if the number is a power of three, `false` otherwise.
 */
fun Int.kompanionIsPowerOfThree(): Boolean {
    var n = this
    if (n <= 0) return false
    while (n % 3 == 0) {
        n /= 3
    }
    return n == 1
}

/**
 * Converts the number to a list of its digits.
 *
 * Example usage:
 * ```Kt
 * println(123.kompanionToDigits()) // Output: [1, 2, 3]
 * ```
 *
 * @return A list of digits as `Int`.
 */
fun Int.kompanionToDigits(): List<Int> = this.toString().map { it.toString().toInt() }

/**
 * Checks if the number is a spy number.
 * A spy number is a number where the sum of its digits equals the product of its digits.
 *
 * Example usage:
 * ```Kt
 * println(1124.kompanionIsSpyNumber()) // Output: true (1 + 1 + 2 + 4 = 8 and 1 * 1 * 2 * 4 = 8)
 * println(123.kompanionIsSpyNumber())  // Output: false
 * ```
 *
 * @return `true` if the number is a spy number, `false` otherwise.
 */
fun Int.kompanionIsSpyNumber(): Boolean {
    val digits = this.kompanionToDigits()
    val sum = digits.sum()
    val product = digits.reduce { acc, i -> acc * i }
    return sum == product
}

/**
 * Checks if the number is an automorphic number.
 * An automorphic number is a number whose square ends with the same digits as the number itself.
 *
 * Example usage:
 * ```Kt
 * println(25.kompanionIsAutomorphic())  // Output: true (25^2 = 625)
 * println(76.kompanionIsAutomorphic())  // Output: true (76^2 = 5776)
 * println(13.kompanionIsAutomorphic())  // Output: false
 * ```
 *
 * @return `true` if the number is automorphic, `false` otherwise.
 */
fun Int.kompanionIsAutomorphic(): Boolean = (this * this).toString().endsWith(this.toString())

/**
 * Returns true if the number is a strong number.
 * A strong number is a number where the sum of the factorial of its digits equals the number itself.
 *
 * Example usage:
 * ```Kt
 * println(145.kompanionIsStrongNumber()) // Output: true (1! + 4! + 5! = 145)
 * println(123.kompanionIsStrongNumber()) // Output: false
 * ```
 *
 * @return `true` if the number is a strong number, `false` otherwise.
 */
fun Int.kompanionIsStrongNumber(): Boolean {
    val factorial = { n: Int -> if (n <= 1) 1 else (2..n).fold(1L) { acc, i -> acc * i } }
    return this == this.kompanionToDigits().sumOf { factorial(it).toInt() }
}

/**
 * Checks if the number is a perfect power (a^b where a > 0 and b > 1).
 *
 * Example usage:
 * ```Kt
 * println(27.kompanionIsPerfectPower()) // Output: true (3^3)
 * println(28.kompanionIsPerfectPower()) // Output: false
 * ```
 *
 * @return `true` if the number is a perfect power, `false` otherwise.
 */
fun Int.kompanionIsPerfectPower(): Boolean {
    if (this <= 1) return false
    for (base in 2..this.kompanionNearestSqrt()) {
        var power = base
        while (power <= this) {
            power *= base
            if (power == this) return true
        }
    }
    return false
}

/**
 * Returns the base-10 logarithm of the number.
 *
 * Example usage:
 * ```Kt
 * println(100.kompanionLog10()) // Output: 2.0
 * ```
 *
 * @return The base-10 logarithm as a `Double`.
 */
fun Int.kompanionLog10(): Double = kotlin.math.log10(this.toDouble())

/**
 * Returns the natural logarithm (base e) of the number.
 *
 * Example usage:
 * ```Kt
 * println(2.kompanionLN()) // Output: 0.6931471805599453
 * ```
 *
 * @return The natural logarithm as a `Double`.
 */
fun Int.kompanionLN(): Double = kotlin.math.ln(this.toDouble())

/**
 * Returns the maximum digit in the number.
 *
 * Example usage:
 * ```Kt
 * println(529.kompanionMaxDigit()) // Output: 9
 * ```
 *
 * @return The maximum digit as an `Int`.
 */
fun Int.kompanionMaxDigit(): Int = this.kompanionToDigits().maxOrNull() ?: 0

/**
 * Returns the minimum digit in the number.
 *
 * Example usage:
 * ```Kt
 * println(529.kompanionMinDigit()) // Output: 2
 * ```
 *
 * @return The minimum digit as an `Int`.
 */
fun Int.kompanionMinDigit(): Int = this.kompanionToDigits().minOrNull() ?: 0

/**
 * Calculates the sum of squares of the digits of the number.
 *
 * Example usage:
 * ```Kt
 * println(123.kompanionSumOfSquaresOfDigits()) // Output: 14 (1^2 + 2^2 + 3^2 = 14)
 * ```
 *
 * @return The sum of the squares of the digits as an `Int`.
 */
fun Int.kompanionSumOfSquaresOfDigits(): Int = this.kompanionToDigits().sumOf { it * it }


/**
 * Returns the nth Fibonacci number.
 *
 * Example usage:
 * ```Kt
 * println(10.kompanionFibonacci()) // Output: 55
 * ```
 */
fun Int.kompanionFibonacci(): Long {
    if (this <= 0) return 0
    if (this == 1 || this == 2) return 1
    var a = 0L
    var b = 1L
    for (i in 2..this) {
        val temp = b
        b += a
        a = temp
    }
    return b
}

/**
 * Returns the digital root of the number (iterative sum of digits until a single digit).
 *
 * Example usage:
 * ```Kt
 * println(1234.kompanionDigitalRoot()) // Output: 1
 * ```
 */
fun Int.kompanionDigitalRoot(): Int {
    var n = this
    while (n >= 10) {
        n = n.kompanionSumOfDigits()
    }
    return n
}

/**
 * Checks if the number is a Harshad (Niven) number (divisible by the sum of its digits).
 *
 * Example usage:
 * ```Kt
 * println(18.kompanionIsHarshad()) // Output: true
 * println(19.kompanionIsHarshad()) // Output: false
 * ```
 */
fun Int.kompanionIsHarshad(): Boolean = this % this.kompanionSumOfDigits() == 0

/**
 * Converts the number to its ordinal string (e.g., 1 -> "1st", 2 -> "2nd").
 *
 * Example usage:
 * ```Kt
 * println(1.kompanionToOrdinal()) // Output: "1st"
 * println(23.kompanionToOrdinal()) // Output: "23rd"
 * ```
 */
fun Int.kompanionToOrdinal(): String {
    if (this % 100 in 11..13) return "${this}th"
    return when (this % 10) {
        1 -> "${this}st"
        2 -> "${this}nd"
        3 -> "${this}rd"
        else -> "${this}th"
    }
}

/**
 * Returns the square root of the number.
 *
 * Example usage:
 * ```Kt
 * println(16.kompanionSqrt()) // Output: 4.0
 * ```
 */
fun Int.kompanionSqrt(): Double = kotlin.math.sqrt(this.toDouble())

/**
 * Converts the number to a Boolean value (0 = false, any other value = true).
 *
 * Example usage:
 * ```Kt
 * println(0.kompanionToBoolean()) // Output: false
 * println(1.kompanionToBoolean()) // Output: true
 * ```
 */
fun Int.kompanionToBoolean(): Boolean = this != 0

/**
 * Returns the sum of the prime factors of the number.
 *
 * Example usage:
 * ```Kt
 * println(12.kompanionSumOfPrimeFactors()) // Output: 7 (2 + 2 + 3)
 * ```
 */
fun Int.kompanionSumOfPrimeFactors(): Int {
    var n = this
    var sum = 0
    var factor = 2
    while (n > 1) {
        while (n % factor == 0) {
            sum += factor
            n /= factor
        }
        factor++
    }
    return sum
}

/**
 * Returns true if the number is an Armstrong number (also known as Narcissistic number).
 * Example: 153 is an Armstrong number because 1^3 + 5^3 + 3^3 = 153.
 *
 * Example usage:
 * ```Kt
 * println(153.kompanionIsArmstrong()) // Output: true
 * println(123.kompanionIsArmstrong()) // Output: false
 * ```
 */
fun Int.kompanionIsArmstrong(): Boolean {
    val digits = this.toString().map { it.toString().toInt() }
    val power = digits.size
    return digits.sumOf { it.toDouble().pow(power).toInt() } == this
}

/**
 * Returns the product of the digits of the number.
 *
 * Example usage:
 * ```Kt
 * println(123.kompanionProductOfDigits()) // Output: 6 (1 * 2 * 3)
 * ```
 */
fun Int.kompanionProductOfDigits(): Int =
    this.toString().map { it.toString().toInt() }.reduce { acc, i -> acc * i }

/**
 * Returns the number of digits in the integer.
 *
 * Example usage:
 * ```Kt
 * println(12345.kompanionCountDigits()) // Output: 5
 * ```
 */
fun Int.kompanionCountDigits(): Int = this.toString().length

/**
 * Checks if the number is a perfect number (equal to the sum of its divisors excluding itself).
 *
 * Example usage:
 * ```Kt
 * println(6.kompanionIsPerfect())  // Output: true (1 + 2 + 3 = 6)
 * println(10.kompanionIsPerfect()) // Output: false
 * ```
 */
fun Int.kompanionIsPerfect(): Boolean = this > 1 && (1 until this).filter { this % it == 0 }.sum() == this

/**
 * Returns the next prime number greater than the current number.
 *
 * Example usage:
 * ```Kt
 * println(10.kompanionNextPrime()) // Output: 11
 * ```
 */
fun Int.kompanionNextPrime(): Int {
    var n = this + 1
    while (!n.kompanionIsPrime()) {
        n++
    }
    return n
}

/**
 * Returns true if the number is a Kaprekar number.
 * A Kaprekar number is a number whose square can be split into two parts that sum to the original number.
 *
 * Example usage:
 * ```Kt
 * println(45.kompanionIsKaprekar()) // Output: true (45^2 = 2025 and 20 + 25 = 45)
 * println(13.kompanionIsKaprekar()) // Output: false
 * ```
 */
fun Int.kompanionIsKaprekar(): Boolean {
    val square = (this * this).toString()
    val splitIndex = square.length / 2
    val left = square.substring(0, splitIndex).toIntOrNull() ?: 0
    val right = square.substring(splitIndex).toIntOrNull() ?: 0
    return left + right == this
}

/**
 * Checks if the number is a Fibonacci number.
 *
 * Example usage:
 * ```Kt
 * println(8.kompanionIsFibonacci())  // Output: true
 * println(10.kompanionIsFibonacci()) // Output: false
 * ```
 */
fun Int.kompanionIsFibonacci(): Boolean {
    val isPerfectSquare = { n: Int ->
        kotlin.math.sqrt(n.toDouble()).toInt().toDouble() == kotlin.math.sqrt(n.toDouble())
    }
    return isPerfectSquare(5 * this * this + 4) || isPerfectSquare(5 * this * this - 4)
}

/**
 * Checks if the number is a palindrome in binary form.
 *
 * Example usage:
 * ```Kt
 * println(9.kompanionIsBinaryPalindrome())  // Output: true ("1001")
 * println(10.kompanionIsBinaryPalindrome()) // Output: false
 * ```
 */
fun Int.kompanionIsBinaryPalindrome(): Boolean {
    val binaryString = this.kompanionToBinaryString()
    return binaryString == binaryString.reversed()
}

/**
 * Returns the number formatted as a human-readable size in bytes, KB, MB, etc.
 *
 * Example usage:
 * ```Kt
 * println(1048576.kompanionToHumanReadableSize()) // Output: "1.0MB"
 * ```
 */
fun Int.kompanionToHumanReadableSize(): String {
    if (this <= 0) return "0B"
    val units = listOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (kotlin.math.log10(this.toDouble()) / kotlin.math.log10(1024.0)).toInt()
    return String.format(
        Locale.US,
        "%.1f%s",
        this / 1024.0.pow(digitGroups.toDouble()),
        units[digitGroups]
    )
}

/**
 * Converts the number into a word representation (works for numbers between 0 and 9999).
 *
 * Example usage:
 * ```Kt
 * println(123.kompanionToWords()) // Output: "One Hundred and Twenty-Three"
 * ```
 */
fun Int.kompanionToWords(): String {
    if (this < 0 || this > 9999) throw IllegalArgumentException("Number out of range (0-9999)")
    val belowTwenty = listOf(
        "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    )
    val tens = listOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")

    return when {
        this < 20 -> belowTwenty[this]
        this < 100 -> "${tens[this / 10]}${if (this % 10 != 0) "-${belowTwenty[this % 10]}" else ""}"
        this < 1000 -> "${belowTwenty[this / 100]} Hundred${if (this % 100 != 0) " and ${(this % 100).kompanionToWords()}" else ""}"
        else -> "${belowTwenty[this / 1000]} Thousand${if (this % 1000 != 0) " ${(this % 1000).kompanionToWords()}" else ""}"
    }
}
