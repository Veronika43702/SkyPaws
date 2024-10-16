package ru.skypaws.mobileapp.data.repository.user

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.datasource.local.user.UserLocalDataSource
import ru.skypaws.mobileapp.data.datasource.remote.UserRemoteDataSource
import ru.skypaws.mobileapp.data.di.utils.IoScope
import ru.skypaws.mobileapp.data.mapper.toDomain
import ru.skypaws.mobileapp.domain.model.UserDomain
import ru.skypaws.mobileapp.domain.repository.user.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    @IoScope private val externalScope: CoroutineScope
) : UserRepository {
    /**
     * Checks whether user is authenticated according to presence of accessToken
     * @return true [Boolean], if accessToken in local storage is not null
     */
    override suspend fun isAuthorized(): Boolean = userLocalDataSource.getAccessToken() != null

    /**
     * Gets user data from local storage by [UserLocalDataSource.getLocalUser()]
     * [ru.skypaws.mobileapp.datasource.local.user.UserLocalDataSource.getLocalUser]
     * @return [UserDomain]
     */
    override suspend fun getLocalUser(): UserDomain =
        userLocalDataSource.getLocalUser()

    /**
     * Clears all user data from local storage by [UserLocalStorage.clearUserData()]
     * [ru.skypaws.mobileapp.datasource.local.user.UserLocalDataSource.clearUserData]
     */
    override suspend fun clearUserData() {
        userLocalDataSource.clearUserData()
    }

    /**
     * Fetches user data from server by api request [ApiService.getUser()][ApiService.getUser]
     * in a separate coroutine to get result even when app is closed or user moves away from the screen.
     *
     * If user.is_verified = true: user is saved locally.
     *
     * If user.is_verified = false: user data is cleared local storage.
     * @return [UserDomain]
     * @throws Exception
     */
    override suspend fun fetchUser(): UserDomain {
        return try {
            externalScope.async {
                val user = userRemoteDataSource.getUser()

                if (user.is_verified) {
                    userLocalDataSource.updateUser(user)
                } else {
                    clearUserData()
                }

                user.toDomain()
            }.await()
        } catch (e: Exception) {
            throw e
        }
    }
}
