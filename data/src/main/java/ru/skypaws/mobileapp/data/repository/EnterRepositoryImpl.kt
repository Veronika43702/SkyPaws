package ru.skypaws.mobileapp.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.di.api.MainApiService
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.data.error.LogErrors
import ru.skypaws.mobileapp.data.datasource.local.user.UserLocalDataSource
import ru.skypaws.mobileapp.data.di.utils.DispatcherIO
import ru.skypaws.mobileapp.domain.repository.EnterRepository
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class EnterRepositoryImpl @Inject constructor(
    @MainApiService private val apiService: ApiService,
    private val userLocalDataSource: UserLocalDataSource,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher
) : EnterRepository {
    /**
     * Gets user id by api request [ApiService.sendCode(username, password, airline)][ApiService.sendCode].
     * @return [String]
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun getResponseForCode(
        username: String,
        password: String,
        airline: Int
    ): String? {
        return withContext(dispatcherIO) {
            try {
                apiService.sendCode(username, password, airline).id
            } catch (e: SocketException) {
                throw e
            } catch (e: UnresolvedAddressException) {
                throw e
            } catch (e: ApiErrors) {
                throw e
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException("EnterRepositoryImpl: getResponseForCode: Exception", e)
                )
                throw e
            }
        }
    }

    /**
     * Registers and fetches [user][ru.skypaws.mobileapp.model.UserDomain] by api request
     * to register [ApiService.register(username, password, code, id)][ApiService.register]
     * and saves it locally by
     * [UserLocalDataSource.saveUser(user)][ru.skypaws.mobileapp.datasource.local.user.UserLocalDataSource.saveUser].
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun register(
        username: String,
        password: String,
        code: String,
        id: String
    ) {
        withContext(dispatcherIO) {
            try {
                val user = apiService.register(id, username, password, code).copy(id = id)

                userLocalDataSource.saveUser(user)

            } catch (e: SocketException) {
                throw e
            } catch (e: UnresolvedAddressException) {
                throw e
            } catch (e: ApiErrors) {
                throw e
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException("EnterRepositoryImpl: register: Exception", e)
                )
                throw e
            }
        }
    }

    /**
     * Signs in and fetches [user][ru.skypaws.mobileapp.model.UserDomain] data
     * by api request to signIn [ApiService.auth(username, password)][ApiService.signIn]
     * and saves it locally by [UserLocalDataSource.saveUser(user)][ru.skypaws.mobileapp.datasource.local.user.UserLocalDataSource.saveUser].
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun signIn(username: String, password: String) {
        withContext(dispatcherIO) {
            try {
                val user = apiService.signIn(username, password)

                userLocalDataSource.saveUser(user)

            } catch (e: SocketException) {
                throw e
            } catch (e: UnresolvedAddressException) {
                throw e
            } catch (e: ApiErrors) {
                throw e
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException("EnterRepositoryImpl: signIn: Exception", e)
                )
                throw e
            }
        }
    }
}
