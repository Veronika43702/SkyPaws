package ru.skypaws.mobileapp.domain.repository

interface ServiceDataRepository {
    /**
     * Fetches price service data and saves it.
     */
    suspend fun fetchPriceInfoFromService()

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
