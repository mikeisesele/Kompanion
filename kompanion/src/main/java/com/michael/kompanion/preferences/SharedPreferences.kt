package com.michael.kompanion.preferences

import android.content.SharedPreferences
import androidx.core.content.edit


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * Provides a convenient way to edit SharedPreferences using a lambda function on the SharedPreferences.Editor.
 * The operation block is executed within the context of the SharedPreferences.Editor.
 *
 * @param operation A lambda function that operates on the SharedPreferences.Editor.
 */
inline fun SharedPreferences.kompanionEdit(operation: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.operation()
    editor.apply()
}

/**
 * Stores a key-value pair in SharedPreferences, inferring the value type.
 * - key: The key for the value to be stored.
 * - value: The value to be stored, which can be of various types (String, Int, Boolean, etc.).
 *
 * @param key The key under which the value is stored.
 * @param value The value to store in SharedPreferences.
 */
fun SharedPreferences.kompanionPut(key: String, value: Any?) {
    when (value) {
        is String -> edit { putString(key, value) }
        is Int -> edit { putInt(key, value) }
        is Boolean -> edit { putBoolean(key, value) }
        is Float -> edit { putFloat(key, value) }
        is Long -> edit { putLong(key, value) }
        // Add more cases as needed
    }
}

/**
 * Retrieves a value from SharedPreferences based on the specified key and infers the return type.
 * - key: The key for the value to be retrieved.
 * - defaultValue: The default value that indicates the type to return.
 *
 * @param key The key for the value to retrieve.
 * @param defaultValue The default value used to infer the return type.
 * @return The retrieved value from SharedPreferences, or the default value if the key does not exist.
 * @throws UnsupportedOperationException If the type is unsupported.
 */
inline fun <reified T> SharedPreferences.kompanionGet(key: String, defaultValue: T): T {
    return when (defaultValue) {
        is String -> getString(key, defaultValue) as T
        is Int -> getInt(key, defaultValue) as T
        is Boolean -> getBoolean(key, defaultValue) as T
        // Add more cases as needed
        else -> throw UnsupportedOperationException("Unsupported type")
    }
}
