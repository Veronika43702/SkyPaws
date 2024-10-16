package ru.skypaws.mobileapp.data.mapper

import ru.skypaws.mobileapp.data.model.dto.UserDto
import ru.skypaws.mobileapp.domain.model.UserDomain

fun UserDto.toDomain(): UserDomain {
    return UserDomain(
        id = this.id,
        name = this.name,
        surname = this.surname,
        position = this.position,
        airline = this.airline,
        company = this.company,
        photo = this.photo,
        isActive = this.is_active,
        isSuperuser = this.is_superuser,
        isVerified = this.is_verified,
        role = this.role,
        apikey = this.apikey,
        accessToken = this.access_token,
        refreshToken = this.refresh_token
    )
}

fun UserDomain.fromDomain(): UserDto {
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