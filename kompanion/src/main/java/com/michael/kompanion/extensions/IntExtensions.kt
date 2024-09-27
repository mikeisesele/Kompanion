package com.michael.kompanion.extensions

fun Int.randomFrom(): Int {
    return (this + 100 .. 1000000000).random()
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
