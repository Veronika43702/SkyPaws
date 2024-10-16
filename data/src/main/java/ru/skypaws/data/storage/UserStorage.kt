package ru.skypaws.data.storage

import ru.skypaws.data.source.dto.PayInfoDto
import ru.skypaws.data.source.dto.UserDto

interface UserStorage {
    fun getAccessToken(): String?
    fun getLocalUser(): UserDto
    fun clearUserData()
    fun saveUser(user: UserDto)
    fun updateUser(user: UserDto)

    fun savePayInfo(payInfoDto: PayInfoDto)
    fun getLogbookExpDate(): Long
    fun getCalendarExpDate(): Long
}