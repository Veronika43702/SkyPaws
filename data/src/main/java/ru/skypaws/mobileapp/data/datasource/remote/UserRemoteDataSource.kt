package ru.skypaws.mobileapp.data.datasource.remote

import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.di.api.MainApiService
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.data.error.LogErrors
import ru.skypaws.mobileapp.data.model.dto.PayInfoDto
import ru.skypaws.mobileapp.data.model.dto.UserDto
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    @MainApiService private val apiService: ApiService,
) {
    suspend fun getUser(): UserDto {
        return try {
            apiService.getUser()
        } catch (e: SocketException) {
            throw e
        } catch (e: UnresolvedAddressException) {
            throw e
        } catch (e: ApiErrors) {
            throw e
        } catch (e: Exception) {
            apiService.sendError(
                LogErrors.fromException("UserRemoteDataSource: getUser: Exception", e)
            )
            throw e
        }
    }

    suspend fun getPayInfo(): PayInfoDto {
        return try {
            apiService.getPayInfo()
        } catch (e: ApiErrors) {
            throw e
        } catch (e: UnknownHostException) {
            throw e
        } catch (e: SocketTimeoutException) {
            throw e
        } catch (e: Exception) {
            apiService.sendError(
                LogErrors.fromException(
                    "UserRemoteDataSource: getPayInfo: Exception", e
                )
            )
            throw e
        }
    }
}