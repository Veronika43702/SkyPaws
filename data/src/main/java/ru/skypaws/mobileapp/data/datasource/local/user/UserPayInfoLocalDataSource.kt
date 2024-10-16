package ru.skypaws.mobileapp.data.datasource.local.user

import ru.skypaws.mobileapp.data.model.dto.PayInfoDto

interface UserPayInfoLocalDataSource {
    suspend fun savePayInfo(payInfoDto: PayInfoDto)
    suspend fun getLogbookExpDate(): Long
    suspend fun getCalendarExpDate(): Long
}