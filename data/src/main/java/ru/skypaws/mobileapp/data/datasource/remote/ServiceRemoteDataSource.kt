package ru.skypaws.mobileapp.data.datasource.remote

import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.di.api.MainApiService
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.data.error.LogErrors
import ru.skypaws.mobileapp.data.model.dto.PricesDto
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class ServiceRemoteDataSource @Inject constructor(
    @MainApiService private val apiService: ApiService,
) {
    suspend fun getPriceInfoFromService(): PricesDto {
        return try {
            apiService.getPriceInfo()
        } catch (e: SocketException) {
            throw e
        } catch (e: UnresolvedAddressException) {
            throw e
        } catch (e: ApiErrors) {
            throw e
        } catch (e: Exception) {
            apiService.sendError(
                LogErrors.fromException(
                    "ServiceDataRepositoryImpl: getPriceInfoFromServer: Exception",
                    e
                )
            )
            throw e
        }
    }
}