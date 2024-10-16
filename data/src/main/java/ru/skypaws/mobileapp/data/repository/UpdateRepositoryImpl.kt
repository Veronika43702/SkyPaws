package ru.skypaws.mobileapp.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.di.api.MainApiService
import ru.skypaws.mobileapp.data.di.utils.DispatcherIO
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.data.error.LogErrors
import ru.skypaws.mobileapp.data.mapper.toDomain
import ru.skypaws.mobileapp.domain.model.Updates
import ru.skypaws.mobileapp.domain.repository.UpdateRepository
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class UpdateRepositoryImpl @Inject constructor(
    @MainApiService private val apiService: ApiService,
    @ApplicationContext private val context: Context,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher
) : UpdateRepository {

    /**
     * Fetches data of latest app and database version kept on server by sending
     * api request [apiService.checkUpdates()][ru.skypaws.mobileapp.datasource.remote.api.ApiService.checkUpdates].
     * @return [Updates]
     *
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun fetchLatestVersionInfo(): Updates {
        return withContext(dispatcherIO) {
            try {
                apiService.checkUpdates().toDomain()
            } catch (e: SocketException) {
                throw e
            } catch (e: UnresolvedAddressException) {
                throw e
            } catch (e: ApiErrors) {
                throw e
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException(
                        "UpdateRepositoryImpl: checkUpdate: Exception", e
                    )
                )
                throw e
            }
        }
    }

    /**
     * Downloads apk file to **context.cacheDir** with new app version from server by sending api
     * request [apiService.downloadApk()][ru.skypaws.mobileapp.datasource.remote.api.ApiService.downloadApk].
     *
     * @return [Flow]<[Float]> representing the progress of the file download in percent
     */
    override suspend fun downloadApk(): Flow<Float> {
        return flow {
            val response = apiService.downloadApk()

            val contentLength = response.contentLength() ?: 0L
            val file = java.io.File(context.cacheDir, "app-release.apk")
            val output = file.outputStream()

            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var totalBytesRead = 0L

            val inputStream = response.bodyAsChannel().toInputStream()

            while (true) {
                val bytesRead = inputStream.read(buffer)
                if (bytesRead <= 0) break
                output.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead
                val progress = (totalBytesRead.toFloat() / contentLength.toFloat()) * 100
                emit(progress)
            }
            output.close()
            inputStream.close()
        }.flowOn(dispatcherIO)
    }
}
