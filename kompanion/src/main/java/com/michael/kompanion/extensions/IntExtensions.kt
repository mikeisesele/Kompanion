package com.michael.kompanion.extensions

fun Int.randomFrom(): Int {
    return (this + 100 .. 1000000000).random()
}
