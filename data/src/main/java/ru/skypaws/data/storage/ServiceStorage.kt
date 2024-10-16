package ru.skypaws.data.storage

interface ServiceStorage {
    fun savePriceInfo(
        logbookPrice: Int,
        syncMonthPrice: Int,
        syncQuarterPrice: Int,
        syncYearPrice: Int
    )

    fun getLogbookPrice(): Int

    fun getCalendarMonthPrice(): Int
    fun getCalendarQuarterPrice(): Int
    fun getCalendarYearPrice(): Int
}