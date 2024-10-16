package ru.skypaws.domain.repository

import ru.skypaws.domain.model.User

interface UserRepository  {
    fun isAuthorized(): Boolean
    fun getLocalUser(): User
    fun clearUserData()

    suspend fun getUser(): User
}