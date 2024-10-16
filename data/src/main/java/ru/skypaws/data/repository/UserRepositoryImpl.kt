package ru.skypaws.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.skypaws.data.api.ApiService
import ru.skypaws.data.di.api.MainApiService
import ru.skypaws.data.error.ApiErrors
import ru.skypaws.data.error.LogErrors
import ru.skypaws.data.mapper.toDomain
import ru.skypaws.data.storage.UserStorage
import ru.skypaws.domain.model.User
import ru.skypaws.domain.repository.UserRepository
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @MainApiService private val apiService: ApiService,
    private val userStorageSharedPrefs: UserStorage
) : UserRepository {


    /**
     * Checks whether user is authenticated according to presence of accessToken
     * @return true [Boolean], if accessToken in SharedPreferences is not null
     */
    override fun isAuthorized(): Boolean {
        val accessToken = userStorageSharedPrefs.getAccessToken()
        return (accessToken != null)
    }


    /**
     * Calls [UserStorageSharedPrefs.getLocalUser()]
     * [ru.skypaws.data.storage.UserStorageSharedPrefs.getLocalUser] to
     * get user data
     * @return [User]
     */
    override fun getLocalUser(): User {
        return userStorageSharedPrefs.getLocalUser().toDomain()
    }

    /**
     * Calls [UserStorageSharedPrefs.clearUserData()]
     * [ru.skypaws.data.storage.UserStorageSharedPrefs.clearUserData] to
     * totally clear all user data from prefAuth
     */
    override fun clearUserData() {
        userStorageSharedPrefs.clearUserData()
    }

    /**
     * Sends api request [ApiService.getUser()][ApiService.getUser] to get user data from server.
     *
     * If user.is_verified = true: user is saved in SharedPreferences.
     *
     * If user.is_verified = false: user data is cleared from SharedPreferences.
     * @return [User]
     * @throws Exception
     */
    override suspend fun getUser(): User {
        return withContext(Dispatchers.IO) {
            try {
                val user = apiService.getUser()

                if (user.is_verified) {
                    userStorageSharedPrefs.updateUser(user)
                } else {
                    clearUserData()
                }

                user.toDomain()
            } catch (e: SocketException) {
                throw Exception()
            } catch (e: UnresolvedAddressException) {
                throw Exception()
            } catch (e: ApiErrors) {
                throw ApiErrors(e.code, e.message)
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException("UserRepositoryImpl: getUser: Exception", e)
                )
                throw Exception()
            }
        }
    }
}
