package com.michael.kompanion.extensions

import kotlin.math.pow

fun Int.randomFrom(): Int {
    return (this + 100..1000000000).random()
}

/**
 * Checks if a number is even.
 */
fun Int.isEven(): Boolean = this % 2 == 0

/**
 * Checks if a number is odd.
 */
fun Int.isOdd(): Boolean = this % 2 != 0


fun Int.random(): Int {
    return (1..this).random()
}

/**
 * Checks if a number is prime.
 */
fun Int.isPrime(): Boolean {
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
 */
inline fun Int.ifInRange(range: IntRange, action: (Int) -> Unit) {
    if (this in range) {
        action(this)
    }
}

/**
 * Returns the factorial of the number.
 */
fun Int.factorial(): Long {
    if (this < 0) return 0
    return (1..this).fold(1L) { acc, i -> acc * i }
}

/**
 * Returns the square of the number.
 */
fun Int.square(): Int = this * this

/**
 * Returns the cube of the number.
 */
fun Int.cube(): Int = this * this * this

/**
 * Converts the number from degrees to radians.
 */
fun Int.toRadians(): Double = Math.toRadians(this.toDouble())

/**
 * Converts the number from radians to degrees.
 */
fun Int.toDegrees(): Double = Math.toDegrees(this.toDouble())

/**
 * Returns the greatest common divisor (GCD) of this number and the given number.
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
 */
fun Int.lcm(other: Int): Int = (this * other) / this.gcd(other)

/**
 * Returns true if the number is a power of two.
 */
fun Int.isPowerOfTwo(): Boolean = this > 0 && (this and (this - 1)) == 0

/**
 * Clamps the number within the provided range.
 */
fun Int.clamp(min: Int, max: Int): Int = when {
    this < min -> min
    this > max -> max
    else -> this
}

/**
 * Returns the absolute value of the number.
 */
fun Int.absoluteValue(): Int = kotlin.math.abs(this)

/**
 * Returns the number raised to the power of [exp].
 */
fun Int.pow(exp: Int): Long {
    if (exp < 0) throw IllegalArgumentException("Exponent must be non-negative")
    return (1..exp).fold(1L) { acc, _ -> acc * this }
}

/**
 * Performs an action a specified number of times.
 */
inline fun Int.repeat(action: () -> Unit) {
    for (i in 1..this) action()
}

/**
 * Returns the percentage value of the number relative to [total].
 */
fun Int.percentOf(total: Int): Double = if (total != 0) (this.toDouble() / total) * 100 else 0.0

/**
 * Returns the number with a specific percentage applied.
 */
fun Int.applyPercentage(percentage: Double): Double = this * (percentage / 100)

/**
 * Returns the integer nearest to this number's square root.
 */
fun Int.nearestSqrt(): Int = kotlin.math.sqrt(this.toDouble()).toInt()

/**
 * Returns the number as a binary string.
 */
fun Int.toBinaryString(): String = Integer.toBinaryString(this)

/**
 * Returns the number as a hexadecimal string.
 */
fun Int.toHexString(): String = Integer.toHexString(this)

/**
 * Checks if the number is a palindrome.
 */
fun Int.isPalindrome(): Boolean {
    val original = this.toString()
    return original == original.reversed()
}

/**
 * Returns the reverse of the number.
 */
fun Int.reverse(): Int = this.toString().reversed().toInt()

/**
 * Converts the number to Roman numerals (valid for numbers 1-3999).
 */
fun Int.toRomanNumerals(): String {
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
 */
fun Int.isPerfectSquare(): Boolean =
    kotlin.math.sqrt(this.toDouble()).let { it == it.toInt().toDouble() }

/**
 * Returns true if the number is a perfect cube.
 */
fun Int.isPerfectCube(): Boolean =
    this.toDouble().pow(1.0 / 3.0).let { it == it.toInt().toDouble() }

/**
 * Converts the number of seconds into a human-readable time format (HH:mm:ss).
 */
fun Int.toTimeFormat(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

/**
 * Rounds the number to the nearest multiple of [multiplier].
 */
fun Int.roundToNearest(multiplier: Int): Int = ((this + multiplier / 2) / multiplier) * multiplier

/**
 * Returns the sum of digits of the number.
 */
fun Int.sumOfDigits(): Int = this.toString().map { it.toString().toInt() }.sum()

/**
 * Converts the number of days into a readable duration (years, months, days).
 */
fun Int.toReadableDuration(): String {
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
 */
fun Int.toFormattedString(): String = "%,d".format(this)

/**
 * Checks if the number is divisible by all provided divisors.
 */
fun Int.isDivisibleByAll(vararg divisors: Int): Boolean = divisors.all { this % it == 0 }

/**
 * Returns the nth Fibonacci number.
 */
fun Int.fibonacci(): Long {
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
 */
fun Int.digitalRoot(): Int {
    var n = this
    while (n >= 10) {
        n = n.sumOfDigits()
    }
    return n
}

/**
 * Checks if the number is a Harshad (Niven) number (divisible by the sum of its digits).
 */
fun Int.isHarshad(): Boolean = this % this.sumOfDigits() == 0

/**
 * Converts the number to its ordinal string (e.g., 1 -> "1st", 2 -> "2nd").
 */
fun Int.toOrdinal(): String {
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
 */
fun Int.sqrt(): Double = kotlin.math.sqrt(this.toDouble())

/**
 * Converts the number to a Boolean value (0 = false, any other value = true).
 */
fun Int.toBoolean(): Boolean = this != 0

/**
 * Returns the sum of the prime factors of the number.
 */
fun Int.sumOfPrimeFactors(): Int {
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
 */
fun Int.isArmstrong(): Boolean {
    val digits = this.toString().map { it.toString().toInt() }
    val power = digits.size
    return digits.sumBy { it.toDouble().pow(power).toInt() } == this
}

/**
 * Returns the product of the digits of the number.
 */
fun Int.productOfDigits(): Int =
    this.toString().map { it.toString().toInt() }.reduce { acc, i -> acc * i }

/**
 * Returns the number of digits in the integer.
 */
fun Int.countDigits(): Int = this.toString().length

/**
 * Checks if the number is a perfect number (equal to the sum of its divisors excluding itself).
 */
fun Int.isPerfect(): Boolean = this > 1 && (1 until this).filter { this % it == 0 }.sum() == this

/**
 * Returns the next prime number greater than the current number.
 */
fun Int.nextPrime(): Int {
    var n = this + 1
    while (!n.isPrime()) {
        n++
    }
    return n
}

/**
 * Returns true if the number is a Kaprekar number.
 * A Kaprekar number is a number whose square can be split into two parts that sum to the original number.
 * Example: 45^2 = 2025, and 20 + 25 = 45.
 */
fun Int.isKaprekar(): Boolean {
    val square = (this * this).toString()
    val splitIndex = square.length / 2
    val left = square.substring(0, splitIndex).toIntOrNull() ?: 0
    val right = square.substring(splitIndex).toIntOrNull() ?: 0
    return left + right == this
}

/**
 * Checks if the number is a Fibonacci number.
 */
fun Int.isFibonacci(): Boolean {
    val isPerfectSquare = { n: Int ->
        kotlin.math.sqrt(n.toDouble()).toInt().toDouble() == kotlin.math.sqrt(n.toDouble())
    }
    return isPerfectSquare(5 * this * this + 4) || isPerfectSquare(5 * this * this - 4)
}

/**
 * Checks if the number is a palindrome in binary form.
 */
fun Int.isBinaryPalindrome(): Boolean {
    val binaryString = this.toBinaryString()
    return binaryString == binaryString.reversed()
}

/**
 * Returns the number formatted as a human-readable size in bytes, KB, MB, etc.
 */
fun Int.toHumanReadableSize(): String {
    if (this <= 0) return "0B"
    val units = listOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (kotlin.math.log10(this.toDouble()) / kotlin.math.log10(1024.0)).toInt()
    return String.format("%.1f%s", this / 1024.0.pow(digitGroups.toDouble()), units[digitGroups])
}

/**
 * Returns true if the number is divisible by either [a] or [b].
 */
fun Int.isDivisibleByEither(a: Int, b: Int): Boolean = this % a == 0 || this % b == 0

/**
 * Converts the number to a string with a specified number of leading zeros.
 */
fun Int.padWithZeros(length: Int): String = this.toString().padStart(length, '0')

/**
 * Returns the harmonic mean of this number and another.
 */
fun Int.harmonicMean(other: Int): Double = 2.0 * this * other / (this + other)

/**
 * Returns the median of the number and two other integers.
 */
fun Int.medianOf(other1: Int, other2: Int): Int = listOf(this, other1, other2).sorted()[1]

/**
 * Calculates the logarithm of the number to a given base.
 */
fun Int.log(base: Int): Double = kotlin.math.log(this.toDouble(), base.toDouble())

/**
 * Returns the average of the number and another integer.
 */
fun Int.average(other: Int): Double = (this + other) / 2.0

/**
 * Checks if the number is a triangular number.
 * A triangular number is one that can form an equilateral triangle.
 */
fun Int.isTriangular(): Boolean {
    val n = (-1 + kotlin.math.sqrt(1.0 + 8 * this)).toInt()
    return n * (n + 1) / 2 == this
}

/**
 * Returns the nth triangular number.
 */
fun Int.nthTriangular(): Int = this * (this + 1) / 2

/**
 * Checks if the number is an abundant number (sum of divisors excluding itself is greater than the number).
 */
fun Int.isAbundant(): Boolean = (1 until this).filter { this % it == 0 }.sum() > this

/**
 * Checks if the number is a deficient number (sum of divisors excluding itself is less than the number).
 */
fun Int.isDeficient(): Boolean = (1 until this).filter { this % it == 0 }.sum() < this


/**
 * Returns true if the number is a happy number.
 * A happy number is defined by repeatedly replacing the number with the sum of the squares of its digits
 * until the number becomes 1 (happy) or falls into a loop (unhappy).
 */
fun Int.isHappy(): Boolean {
    var num = this
    val seen = mutableSetOf<Int>()
    while (num != 1 && !seen.contains(num)) {
        seen.add(num)
        num = num.toString().map { it.toString().toInt().pow(2) }.sum().toInt()
    }
    return num == 1
}


/**
 * Returns true if the number is a power of three.
 */
fun Int.isPowerOfThree(): Boolean {
    var n = this
    if (n <= 0) return false
    while (n % 3 == 0) {
        n /= 3
    }
    return n == 1
}

/**
 * Converts the number to a list of its digits.
 */
fun Int.toDigits(): List<Int> = this.toString().map { it.toString().toInt() }

/**
 * Checks if the number is a spy number.
 * A spy number is a number where the sum of its digits equals the product of its digits.
 */
fun Int.isSpyNumber(): Boolean {
    val digits = this.toDigits()
    return digits.sum() == digits.reduce { acc, i -> acc * i }
}

/**
 * Checks if the number is an automorphic number.
 * An automorphic number is a number whose square ends with the same digits as the number itself.
 * Example: 25^2 = 625, which ends in 25.
 */
fun Int.isAutomorphic(): Boolean = (this * this).toString().endsWith(this.toString())


/**
 * Returns true if the number is a strong number.
 * A strong number is a number where the sum of the factorial of its digits equals the number itself.
 * Example: 145 = 1! + 4! + 5!.
 */
fun Int.isStrongNumber(): Boolean {
    val factorial = { n: Int -> (1..n).fold(1L) { acc, i -> acc * i } }
    return this == this.toDigits().sumBy { factorial(it).toInt() }
}

/**
 * Checks if the number is a perfect power (a^b where a > 0 and b > 1).
 */
fun Int.isPerfectPower(): Boolean {
    if (this <= 1) return false
    for (base in 2..this.sqrt().toInt()) {
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
 */
fun Int.log10(): Double = kotlin.math.log10(this.toDouble())

/**
 * Returns the natural logarithm (base e) of the number.
 */
fun Int.ln(): Double = kotlin.math.ln(this.toDouble())

/**
 * Converts the number into a word representation (works for numbers between 0 and 9999).
 */
fun Int.toWords(): String {
    if (this < 0 || this > 9999) throw IllegalArgumentException("Number out of range (0-9999)")
    val belowTwenty = listOf(
        "Zero",
        "One",
        "Two",
        "Three",
        "Four",
        "Five",
        "Six",
        "Seven",
        "Eight",
        "Nine",
        "Ten",
        "Eleven",
        "Twelve",
        "Thirteen",
        "Fourteen",
        "Fifteen",
        "Sixteen",
        "Seventeen",
        "Eighteen",
        "Nineteen"
    )
    val tens =
        listOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")

    return when {
        this < 20 -> belowTwenty[this]
        this < 100 -> "${tens[this / 10]}${if (this % 10 != 0) "-${belowTwenty[this % 10]}" else ""}"
        this < 1000 -> "${belowTwenty[this / 100]} Hundred${if (this % 100 != 0) " and ${(this % 100).toWords()}" else ""}"
        else -> "${belowTwenty[this / 1000]} Thousand${if (this % 1000 != 0) " ${(this % 1000).toWords()}" else ""}"
    }
}

/**
 * Returns the maximum digit in the number.
 */
fun Int.maxDigit(): Int = this.toDigits().maxOrNull() ?: 0

/**
 * Returns the minimum digit in the number.
 */
fun Int.minDigit(): Int = this.toDigits().minOrNull() ?: 0

/**
 * Calculates the sum of squares of the digits of the number.
 */
fun Int.sumOfSquaresOfDigits(): Int = this.toDigits().sumBy { it * it }


