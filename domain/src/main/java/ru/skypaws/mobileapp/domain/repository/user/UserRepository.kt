package ru.skypaws.mobileapp.domain.repository.user

import ru.skypaws.mobileapp.domain.model.UserDomain

interface UserRepository {
    /**
     * Fetches user data from server.
     * @return [UserDomain]
     */
    suspend fun fetchUser(): UserDomain

    /**
     * Checks whether user is authenticated according to presence of accessToken.
     * @return [Boolean]
     */
    suspend fun isAuthorized(): Boolean

    /**
     * Gets user data from local storage.
     * @return [UserDomain]
     */
    suspend fun getLocalUser(): UserDomain

    /**
     * Clears all user data from local storage.
     */
    suspend fun clearUserData()
}