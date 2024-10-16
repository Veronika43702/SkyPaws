package ru.skypaws.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.skypaws.data.api.ApiService
import ru.skypaws.data.di.api.MainApiService
import ru.skypaws.data.error.ApiErrors
import ru.skypaws.data.error.LogErrors
import ru.skypaws.data.mapper.toDomain
import ru.skypaws.domain.model.Updates
import ru.skypaws.domain.repository.UpdateRepository
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class UpdateRepositoryImpl @Inject constructor(
    @MainApiService private val apiService: ApiService,
    @ApplicationContext private val context: Context,
) : UpdateRepository {

    /**
     * Sends api request [apiService.checkUpdates()][ru.skypaws.data.api.ApiService.checkUpdates] and
     * returns update data of app and database versions
     * @return [Updates]
     *
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun checkUpdate(): Updates {
        return withContext(Dispatchers.IO) {
            try {
                apiService.checkUpdates().toDomain()
            } catch (e: SocketException) {
                throw Exception()
            } catch (e: UnresolvedAddressException) {
                throw Exception()
            } catch (e: ApiErrors) {
                throw ApiErrors(e.code, e.message)
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException(
                        "UpdateRepositoryImpl: checkUpdate: Exception", e
                    )
                )
                throw Exception()
            }
        }
    }

    /**
     * Sends api request [apiService.downloadApk()][ru.skypaws.data.api.ApiService.downloadApk] to
     * get **app-release.apk** and save it in **context.cacheDir**.
     *
     * @return [Flow] of [Float] representing the progress of the file download in percent
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
        }.flowOn(Dispatchers.IO)
    }
}
