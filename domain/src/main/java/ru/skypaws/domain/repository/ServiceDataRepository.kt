package ru.skypaws.domain.repository

interface ServiceDataRepository {
    suspend fun getPriceInfoFromService()

    fun getLogbookPrice(): Int
    fun getCalendarMonthPrice(): Int
    fun getCalendarQuarterPrice(): Int
    fun getCalendarYearPrice(): Int
}
