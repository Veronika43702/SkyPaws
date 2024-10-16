package ru.skypaws.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.skypaws.data.api.ApiService
import ru.skypaws.data.di.api.MainApiService
import ru.skypaws.data.error.ApiErrors
import ru.skypaws.data.error.LogErrors
import ru.skypaws.data.storage.UserStorage
import ru.skypaws.domain.repository.EnterRepository
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class EnterRepositoryImpl @Inject constructor(
    @MainApiService private val apiService: ApiService,
    private val userStorageSharedPrefs: UserStorage
) : EnterRepository {
    /**
     * Sends api request to get code data [ApiService.sendCode(username, password, airline)][ApiService.sendCode]
     * and gets user id.
     * @return [String]
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun getResponseForCode(
        username: String,
        password: String,
        airline: Int
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                apiService.sendCode(username, password, airline).id
            } catch (e: SocketException) {
                throw Exception()
            } catch (e: UnresolvedAddressException) {
                throw Exception()
            } catch (e: ApiErrors) {
                throw ApiErrors(e.code, e.message)
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException("EnterRepositoryImpl: getResponseForCode: Exception", e)
                )
                throw Exception()
            }
        }
    }

    /**
     * Sends api request to register [ApiService.register(username, password, code, id)][ApiService.register]
     * to get [user][ru.skypaws.domain.model.User], saving it to SharedPreferences
     * [UserStorageSharedPrefs.saveUser(user)][ru.skypaws.data.storage.UserStorageSharedPrefs.saveUser].
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun register(
        username: String,
        password: String,
        code: String,
        id: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                val user = apiService.register(id, username, password, code).copy(id = id)

                userStorageSharedPrefs.saveUser(user)

            } catch (e: SocketException) {
                throw Exception()
            } catch (e: UnresolvedAddressException) {
                throw Exception()
            } catch (e: ApiErrors) {
                throw ApiErrors(e.code, e.message)
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException("EnterRepositoryImpl: register: Exception", e)
                )
                throw Exception()
            }
        }
    }

    /**
     * Sends api request to signIn [ApiService.auth(username, password)][ApiService.auth]
     * to get [user][ru.skypaws.domain.model.User], saving it to SharedPreferences
     * [UserStorageSharedPrefs.saveUser(user)][ru.skypaws.data.storage.UserStorageSharedPrefs.saveUser].
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun signIn(username: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                val user = apiService.auth(username, password)

                userStorageSharedPrefs.saveUser(user)

            } catch (e: SocketException) {
                throw Exception()
            } catch (e: UnresolvedAddressException) {
                throw Exception()
            } catch (e: ApiErrors) {
                throw ApiErrors(e.code, e.message)
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException("EnterRepositoryImpl: signIn: Exception", e)
                )
                throw Exception()
            }
        }
    }
}
