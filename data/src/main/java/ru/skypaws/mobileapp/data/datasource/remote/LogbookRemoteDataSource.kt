package ru.skypaws.mobileapp.data.datasource.remote

import ru.skypaws.mobileapp.data.datasource.remote.api.ApiAviabitService
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.di.api.AviabitApiService
import ru.skypaws.mobileapp.data.di.api.MainApiService
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.data.error.LogErrors
import ru.skypaws.mobileapp.data.model.dto.LogbookDto
import java.io.IOException
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class LogbookRemoteDataSource @Inject constructor(
    @MainApiService private val apiService: ApiService,
    @AviabitApiService private val apiAviabitService: ApiAviabitService,
) {
    suspend fun getLogbook(): List<LogbookDto> {
        return try {
            apiAviabitService.fetchLogbook(false)
        } catch (e: SocketException) {
            throw ApiErrors(-1, null)
        } catch (e: UnresolvedAddressException) {
            throw ApiErrors(-1, null)
        } catch (e: ApiErrors) {
            throw e
        } catch (e: IOException) {
            apiService.sendError(
                LogErrors.fromException("LogbookRemoteDataSource: getLogbook: IOException", e)
            )
            throw ApiErrors(-1, null)
        } catch (e: Exception) {
            apiService.sendError(
                LogErrors.fromException("LogbookRemoteDataSource: getLogbook: Exception", e)
            )
            throw ApiErrors(-1, null)
        }
    }
}