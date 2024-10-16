package ru.skypaws.domain.repository

interface UserPayInfoRepository  {
    suspend fun getPayInfo()

    fun getLogbookExpDate(): Long
    fun getCalendarExpDate(): Long
}