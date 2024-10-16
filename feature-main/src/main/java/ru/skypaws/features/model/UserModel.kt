package ru.skypaws.features.model

data class UserModel (
    val loading: Boolean = false,
    val userInfoLoaded: Boolean = false,
    val userLocalLoaded: Boolean = false,
    val userNotVerified: Boolean = false,
    val errorGetUser: Boolean = false,
)