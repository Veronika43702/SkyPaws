package ru.skypaws.mobileapp.data.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class FileStorageService @Inject constructor(
    @ApplicationContext private val context: Context
){
    // transform path uri to human view
    /**
     * Creates URI type from string [uri] and takes last part to transform URI to human view.
     * @param uri string type uri
     * @return [String] or null in case [uri] == null
     */
    fun transformPath(uri: String?): String? {
        return if (uri != null) {
            // path segments (usually two: "tree" and else part)
            val pathSegments = uri.toUri().pathSegments

            // "primary" is contained for path in android system
            return if (uri.contains("primary")) {
                pathSegments.subList(1, pathSegments.size)
                    .joinToString("/")
                    .replace(":", "/")
                    .substringAfter("primary")

            } else {
                uri
                    .substringAfter("//")
                    .substringBefore("tree")
            }
        } else null
    }

    /**
     * Extracts file name from [contentDisposition].
     * @return [String]
     */
    fun extractFileName(contentDisposition: String): String {
        val fileNameRegex = "filename=\"([^\"]+)\"".toRegex()
        val matchResult = fileNameRegex.find(contentDisposition)
        return matchResult?.groupValues?.get(1) ?: "logbook.pdf"
    }

    /**
     * Uses [Intent.ACTION_VIEW] to install app-release.apk from context.cacheDir
     * with mimeType *application/vnd.android.package-archive*.
     *
     * Intent has flags [FLAG_GRANT_READ_URI_PERMISSION][Intent.FLAG_GRANT_READ_URI_PERMISSION] and
     * [FLAG_ACTIVITY_NEW_TASK][Intent.FLAG_ACTIVITY_NEW_TASK].
     *
     * There's check for [canRequestPackageInstalls][android.content.pm.PackageManager.canRequestPackageInstalls]
     * and in case of **false** request permission using [ACTION_MANAGE_UNKNOWN_APP_SOURCES][Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES]
     *
     * @param activity The activity context.
     */
    fun installApk(activity: Activity) {
        val apkFile = File(context.cacheDir, "app-release.apk")
        val intent = Intent(Intent.ACTION_VIEW)
        val mimeType = "application/vnd.android.package-archive"

        intent.setDataAndType(
            FileProvider.getUriForFile(context, "${context.packageName}.provider", apkFile),
            mimeType
        )
        intent.flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK

        // Check if the app has permission to install packages from unknown sources
        if (context.packageManager.canRequestPackageInstalls()) {
            context.startActivity(intent)
        } else {
            // Request permission to install packages from unknown sources
            val installPermissionIntent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            installPermissionIntent.data = Uri.parse("package:${context.packageName}")
            activity.startActivity(installPermissionIntent)
        }
    }

    /**
     * Copying file from temporary URI (**cacheDir**) to the [uri] with [filename] name.
     * After copying temporary file in **cacheDir** is deleted.
     * @param [filename] file name to save file with it.
     * @param [uri] uri to which file has to be copied.
     */
    fun copyFileFromTemp(filename: String, uri: Uri) {
        val tempFile = File(context.cacheDir, filename)
        val inputStream = tempFile.inputStream()
        val outputStream = context.contentResolver.openOutputStream(uri)

        // copying file from temporary path (cacheDir) to chosen by User
        inputStream.use { input ->
            outputStream?.use { output ->
                input.copyTo(output)
            }
        }

        // Delete the temporary file after copying
        tempFile.delete()
    }
}