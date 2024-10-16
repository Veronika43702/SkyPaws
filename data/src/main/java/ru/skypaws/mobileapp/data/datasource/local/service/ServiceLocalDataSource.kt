package ru.skypaws.mobileapp.data.datasource.local.service

interface ServiceLocalDataSource {
    /**
     * Saves price service data to local storage.
     * @param logbookPrice price for logbook
     * @param syncMonthPrice price for calendar for month
     * @param syncQuarterPrice price for calendar for quarter
     * @param syncYearPrice price for calendar for year
     */
    suspend fun savePriceInfo(
        logbookPrice: Int,
        syncMonthPrice: Int,
        syncQuarterPrice: Int,
        syncYearPrice: Int
    )

    /**
     * Gets LogbookPrice data from local storage.
     * @return [Int]
     */
    suspend fun getLogbookPrice(): Int

    /**
     * Gets CalendarMonthPrice data from local storage
     * @return [Int]
     */
    suspend fun getCalendarMonthPrice(): Int

    /**
     * Gets CalendarQuarterPrice data from local storage
     * @return [Int]
     */
    suspend fun getCalendarQuarterPrice(): Int

    /**
     * Gets CalendarYearPrice data from local storage
     * @return [Int]
     */
    suspend fun getCalendarYearPrice(): Int
}