package ru.skypaws.features.mainActivity.model

data class UserModel (
    val loading: Boolean = false,
    val userInfoLoaded: Boolean = false,
    val userNotVerified: Boolean = false,
    val errorGetUser: Boolean = false,
)