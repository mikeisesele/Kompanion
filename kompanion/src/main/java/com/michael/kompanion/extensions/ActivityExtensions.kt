package com.michael.kompanion.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Parcelable
import com.michael.kompanion.utils.safeNullableReturnableOperation
import java.io.File
import java.io.FileOutputStream
import java.io.Serializable

/**
 * @param flags Optional flags to be added to the Intent.
 * @param extras Optional extras to be added to the Intent.
 *
 * Example of how to use navigateTo function:
 * Navigate from MainActivity to SecondActivity with FLAG_ACTIVITY_CLEAR_TOP flag
 * navigateTo<SecondActivity>(flags = Intent.FLAG_ACTIVITY_CLEAR_TOP)
 *
 */
inline fun <reified T : Activity> Context.navigateTo(
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
fun Activity.readFromAssets(fileName: String) : String {
    return safeNullableReturnableOperation(
        operation = {
            assets.open(fileName)
                .bufferedReader()
                .use {
                    with(it) {
                        readText()
                    }
                }
        },
        exceptionMessage = "ReadFromAssets Error"
    ).orEmpty()
}

fun Activity.finishWithResult(resultCode: Int, resultIntent: Intent? = null) {
    setResult(resultCode, resultIntent)
    finish()
}


fun Activity.takeScreenshot(): Bitmap? {
    val view = window.decorView.rootView
    view.isDrawingCacheEnabled = true
    val bitmap = Bitmap.createBitmap(view.drawingCache)
    view.isDrawingCacheEnabled = false
    return bitmap
}

fun Activity.takeScreenshotAsFile(): File? {
    val bitmap = takeScreenshot() ?: return null
    val file = File(externalCacheDir, "screenshot_${System.currentTimeMillis()}.png")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.close()
    return file
}
