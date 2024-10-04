package com.michael.kompanion.extensions

import android.location.Location
import kotlin.random.Random
import kotlin.math.*


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */


/*
 * Generates a URL for google maps to show a location.
 */
fun Location.kompanionToMapUrl(): String {
    return "https://maps.google.com/maps?q=${latitude},${longitude}"
}

/**
 * Converts degrees to radians.
 */
fun Double.kompanionToRadians(): Double = Math.toRadians(this)

/**
 * Converts radians to degrees.
 */
fun Double.kompanionToDegrees(): Double = Math.toDegrees(this)
