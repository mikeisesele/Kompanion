package com.michael.kompanion.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

const val MIME_PDF = "application/pdf"


/**
 * Downloads a file from the specified URL and saves it to the designated directory.
 *
 * @param fileName The name of the file to be saved.
 * @param desc A description for the download notification.
 * @param url The URL of the file to download.
 * @param destinationDir The directory where the file will be saved. Default is the Downloads directory.
 * @return The download ID for the queued download.
 */
fun Context.kompanionDownloadFile(
    fileName: String,
    desc: String,
    url: String,
    destinationDir: String = Environment.DIRECTORY_DOWNLOADS,
): Long {
    val request = DownloadManager.Request(Uri.parse(url))
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        .setTitle(fileName)
        .setDescription(desc)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)
        .setDestinationInExternalPublicDir(destinationDir, fileName)
    val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    return downloadManager.enqueue(request)
}

/**
 * Converts a file URI into a content URI, handling the differences between Android versions.
 * If Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q, a content:// URI is returned directly.
 * Otherwise, a file:// URI is converted into a content URI using FileProvider.
 *
 * @param uri The URI of the file to convert.
 * @return The content URI for the file.
 */
fun Context.kompanionGetFileUri(uri: String): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Uri.parse(uri)
    } else {
        FileProvider.getUriForFile(this, packageName, File(Uri.parse(uri).path!!))
    }
}

/**
 * Opens a file using an Intent, specifying the MIME type if provided.
 * Handles any errors by executing the onErrorAction lambda.
 *
 * @param uri The URI of the file to open.
 * @param mimeDataType The MIME type of the file (optional).
 * @param onErrorAction Action to perform if an error occurs when trying to open the file.
 */
fun Context.kompanionOpenFile(
    uri: String,
    mimeDataType: String? = null,
    onErrorAction: () -> Unit,
) {
    with(Intent(Intent.ACTION_VIEW)) {
        if (!mimeDataType.isNullOrBlank()) {
            setDataAndType(
                kompanionGetFileUri(uri),
                mimeDataType,
            )
        } else {
            data = kompanionGetFileUri(uri)
        }
        addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_GRANT_READ_URI_PERMISSION,
        )
        try {
            startActivity(this)
        } catch (e: Exception) {
            onErrorAction()
        }
    }
}
