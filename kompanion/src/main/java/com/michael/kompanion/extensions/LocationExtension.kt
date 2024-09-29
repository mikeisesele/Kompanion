package com.michael.kompanion.extensions

import android.location.Location
import kotlin.random.Random
import kotlin.math.*


fun Location.toMapUrl(): String {
    return "https://maps.google.com/maps?q=${latitude},${longitude}"
}

/**
 * Converts degrees to radians.
 */
fun Double.toRadians(): Double = Math.toRadians(this)

/**
 * Converts radians to degrees.
 */
fun Double.toDegrees(): Double = Math.toDegrees(this)
