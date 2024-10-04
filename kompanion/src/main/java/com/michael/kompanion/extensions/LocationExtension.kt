package com.michael.kompanion.extensions

import android.location.Location
import kotlin.random.Random
import kotlin.math.*

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
