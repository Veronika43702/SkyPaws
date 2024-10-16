package ru.skypaws.mobileapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val position: String? = null,
    val airline: Int = 1,
    val company: String? = null,
    val photo: String? = null,
    val is_active: Boolean = false,
    val is_superuser: Boolean = false,
    val is_verified: Boolean = false,
    val role: Int = -1,
    val apikey: String? = null,
    val access_token: String? = null,
    val refresh_token: String? = null,
)