package com.michael.kompanion.extensions

import android.location.Location

fun Location.toMapUrl(): String {
    return "https://maps.google.com/maps?q=${latitude},${longitude}"
}
