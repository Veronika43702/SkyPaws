package ru.skypaws.mobileapp.data.datasource.local.user

import ru.skypaws.mobileapp.data.model.dto.UserDto
import ru.skypaws.mobileapp.domain.model.UserDomain

interface UserLocalDataSource {
    suspend fun getAccessToken(): String?
    suspend fun getApiKey(): String?
    suspend fun getLocalUser(): UserDomain
    suspend fun clearUserData()
    suspend fun saveUser(user: UserDto)
    suspend fun updateUser(user: UserDto)
}