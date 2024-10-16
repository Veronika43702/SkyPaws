package ru.skypaws.domain.model

data class User(
    val id: String = "0",
    val name: String? = null,
    val surname: String? = null,
    val position: String? = null,
    val airline: Int = 1,
    val company: String? = null,
    val photo: String? = null,
    val isActive: Boolean = false,
    val isSuperuser: Boolean = false,
    val isVerified: Boolean = false,
    val role: Int = -1,
    val apikey: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
)