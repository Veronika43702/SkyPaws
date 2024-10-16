package ru.skypaws.data.mapper

import ru.skypaws.data.source.dto.UserDto
import ru.skypaws.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = this.id ?: "0",
        name = this.name ?: "",
        surname = this.surname ?: "",
        position = this.position ?: "",
        airline = this.airline,
        company = this.company ?: "",
        photo = this.photo ?: "",
        isActive = this.is_active,
        isSuperuser = this.is_superuser,
        isVerified = this.is_verified,
        role = this.role,
        apikey = this.apikey ?: "",
        accessToken = this.access_token ?: "",
        refreshToken = this.refresh_token ?: ""
    )
}

fun User.fromDomain(): UserDto {
    return UserDto(
        id = this.id,
        name = this.name,
        surname = this.surname,
        position = this.position,
        airline = this.airline,
        company = this.company,
        photo = this.photo,
        is_active = this.isActive,
        is_superuser = this.isSuperuser,
        is_verified = this.isVerified,
        role = this.role,
        apikey = this.apikey,
        access_token = this.accessToken,
        refresh_token = this.refreshToken
    )
}