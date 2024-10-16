package ru.skypaws.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.statement.readBytes
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.skypaws.data.api.ApiService
import ru.skypaws.data.di.api.MainApiService
import ru.skypaws.data.error.ApiErrors
import ru.skypaws.data.error.LogErrors
import ru.skypaws.data.utils.FileStorageService
import ru.skypaws.domain.repository.DownloadLogbookRepository
import ru.skypaws.domain.usecase.settings.GetPathUseCase
import java.io.File
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class DownloadLogbookRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @MainApiService private val apiService: ApiService,
    private val fileServiceStorage: FileStorageService,
    private val getPathUseCase: GetPathUseCase,
) : DownloadLogbookRepository {

    /**
     * Sends api request [apiService.downloadLogbook()][ru.skypaws.data.api.ApiService.downloadLogbook] to
     * get HttpResponse and save file with name from [contentDisposition][String] to **context.cacheDir**.
     * @return [String]: contentDisposition ?: logbook.pdf
     * @throws Exception
     * @throws ApiErrors
     */
    override suspend fun downloadLogbook(): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.downloadLogbook()

                if (!response.status.isSuccess()) {
                    throw ApiErrors(response.status.value, response.status.description)
                }

                val responseBody = response.readBytes()

                val contentDisposition = response.headers["Content-Disposition"]
                //saving the response body temporarily to cacheDir with fileName from contentDisposition
                val tempFile = File(context.cacheDir, contentDisposition ?: "")
                tempFile.outputStream().use { output ->
                    output.write(responseBody)
                }

                // extracting file name from contentDisposition
                contentDisposition?.let { fileServiceStorage.extractFileName(it) }
                    ?: "logbook.pdf"
            } catch (e: IOException) {
                apiService.sendError(
                    LogErrors.fromException(
                        "DownloadLogbookRepositoryImpl: downloadLogbook: IOException", e
                    )
                )
                throw Exception(e)
            } catch (e: UnresolvedAddressException) {
                throw Exception()
            } catch (e: ApiErrors) {
                throw ApiErrors(e.code, e.message)
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException(
                        "DownloadLogbookRepositoryImpl: downloadLogbook: Exception", e
                    )
                )
                throw Exception(e)
            }
        }
    }

    /**
     * Checks presence of path in SharedPreferences by
     * [SettingsStorage.getPath()][ru.skypaws.data.storage.SettingsStorage.getPath].
     *
     * Checks that path exists on Android system:
     *
     *              if (documentFile == null || !documentFile.canWrite()) {
     *                 false
     *             } else {
     *                 true
     *             }
     * @return [Boolean]:
     * - **true**, if path exists in SharesPreferences and Android system
     * - **false** if path absent in SharesPreferences or in Android system,
     * or it's impossible to write to the URI
     */
    override fun isPathSet(): Boolean {
        val uriString = getPathUseCase()

        return if (uriString == null) {
            // path is not set by user in settings or path is incorrect/does not exists
            false
        } else {
            val uri = Uri.parse(uriString)
            val documentFile = DocumentFile.fromTreeUri(context, uri)

            if (documentFile == null || !documentFile.canWrite()) {
                // path is set in settings but directory does not exist
                // (folder is deleted or other error)
                false
            } else {
                // path is set in settings and directory exists
                true
            }
        }
    }

    /**
     * Copying file from temporary URI (**cacheDir**) to the [uriString] with [filename] name by calling
     * [FileStorageService.copyFileFromTemp()][ru.skypaws.data.utils.FileStorageService.copyFileFromTemp]
     * @param [uriString] uri of [String] type.
     * @param [filename] file name to save file with it
     * @throws Exception
     */
    override suspend fun saveLogbook(uriString: String, filename: String) {
        withContext(Dispatchers.IO) {
            try {
                fileServiceStorage.copyFileFromTemp(filename, uriString.toUri())
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException(
                        "DownloadLogbookRepositoryImpl: saveLogbook: Exception",
                        e
                    )
                )
                throw Exception(e)
            }
        }
    }

    /**
     * Copying file from temporary URI (**cacheDir**) with [filename] name to
     * [path][ru.skypaws.data.repository.SettingsRepositoryImpl.getPath] by calling
     * [FileStorageService.copyFileFromTemp()][ru.skypaws.data.utils.FileStorageService.copyFileFromTemp].
     *
     * Throws [Exception] in case path
     * - does NOT exist in Android system,
     * - is not possible to write to it
     * - exception during file creation
     * @param [filename] file name to save file with it
     * @throws Exception
     */
    override suspend fun saveLogbookToChosenPath(filename: String) {
        withContext(Dispatchers.IO) {
            try {
                val uriStringTempFile = getPathUseCase() ?: throw Exception("No path selected")
                val uriTempFile = uriStringTempFile.toUri()

                // DocumentFile to interact with the URI
                val documentFile = DocumentFile.fromTreeUri(context, uriTempFile)
                    ?: throw Exception("Invalid directory URI")


                // Checking if the directory is writable after ensuring the structure
                if (!documentFile.canWrite()) {
                    throw Exception("Cannot write to the selected directory")
                }

                // Create a new file in the selected directory
                val newFile =
                    documentFile.createFile("application/octet-stream", filename)
                        ?: throw Exception("Failed to create new file in the selected directory")

                fileServiceStorage.copyFileFromTemp(filename, newFile.uri)
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException(
                        "DownloadLogbookRepositoryImpl: saveLogbookToChosenPath: Exception",
                        e
                    )
                )
                throw Exception()
            }
        }
    }
}
