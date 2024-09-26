package com.michael.kompanion.preferences

import android.content.SharedPreferences

inline fun SharedPreferences.edit(operation: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.operation()
    editor.apply()
}

fun SharedPreferences.put(key: String, value: Any?) {
    when (value) {
        is String -> edit { putString(key, value) }
        is Int -> edit { putInt(key, value) }
        is Boolean -> edit { putBoolean(key, value) }
        is Float -> edit { putFloat(key, value) }
        is Long -> edit { putLong(key, value) }
        // Add more cases as needed
    }
}

inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    return when (defaultValue) {
        is String -> getString(key, defaultValue) as T
        is Int -> getInt(key, defaultValue) as T
        is Boolean -> getBoolean(key, defaultValue) as T
        // Add more cases as needed
        else -> throw UnsupportedOperationException("Unsupported type")
    }
}

