package com.michael.kompanion.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Parcelable
import com.michael.kompanion.utils.kompanionSafeNullableReturnableOperation
import java.io.File
import java.io.FileOutputStream
import java.io.Serializable


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * @param flags Optional flags to be added to the Intent
 * @param extras Optional extras to be added to the Intent.
 *
 *
 * ```Kt
 *
 * Navigate from MainActivity to SecondActivity with FLAG_ACTIVITY_CLEAR_TOP flag
 * navigateTo<SecondActivity>(flags = Intent.FLAG_ACTIVITY_CLEAR_TOP)
 * ```
 *
 */
inline fun <reified T : Activity> Context.kompanionNavigateTo(
    vararg flags: Int,
    extras: List<Pair<String, Any>> = emptyList()
) {
    val intent = Intent(applicationContext, T::class.java)
    flags.forEach { intent.addFlags(it) }

    extras.forEach { (key, value) ->
        when (value) {
            is String -> intent.putExtra(key, value)
            is Int -> intent.putExtra(key, value)
            is Boolean -> intent.putExtra(key, value)
            is Byte -> intent.putExtra(key, value)
            is Char -> intent.putExtra(key, value)
            is Short -> intent.putExtra(key, value)
            is Long -> intent.putExtra(key, value)
            is Float -> intent.putExtra(key, value)
            is Double -> intent.putExtra(key, value)
            is Serializable -> intent.putExtra(key, value)
            is Parcelable -> intent.putExtra(key, value)
            // Add more cases for other types as needed
        }
    }

    startActivity(intent)
}


/*
    function to read from assets folder. fileName would be filename.json
 */
fun Activity.kompanionReadFromAssets(fileName: String) : String {
    return kompanionSafeNullableReturnableOperation(
        operation = {
            assets.open(fileName)
                .bufferedReader()
                .use {
                    with(it) {
                        readText()
                    }
                }
        },
    ).orEmpty()
}


/*
    function to finish activity with result code and result intent
 */
fun Activity.kompanionFinishActivityWithResult(resultCode: Int, resultIntent: Intent? = null) {
    setResult(resultCode, resultIntent)
    finish()
}

/*
    function to take screenshot of the activity
 */
fun Activity.kompanionTakeScreenshot(): Bitmap? {
    val view = window.decorView.rootView
    view.isDrawingCacheEnabled = true
    val bitmap = Bitmap.createBitmap(view.drawingCache)
    view.isDrawingCacheEnabled = false
    return bitmap
}

/*
    function to take screenshot of the activity and save it to the external cache directory
 */
fun Activity.kompanionTakeScreenshotAsFile(): File? {
    val bitmap = kompanionTakeScreenshot() ?: return null
    val file = File(externalCacheDir, "screenshot_${System.currentTimeMillis()}.png")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.close()
    return file
}
