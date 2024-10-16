package ru.skypaws.mobileapp.domain.repository.logbook

import ru.skypaws.mobileapp.domain.model.FlightTime
import ru.skypaws.mobileapp.domain.model.MonthType
import ru.skypaws.mobileapp.domain.model.YearMonth
import ru.skypaws.mobileapp.domain.model.YearMonthType
import ru.skypaws.mobileapp.domain.model.YearType

interface LogbookRepository {
    /**
     * Fetches logbook data from remote source and saves it locally.
     */
    suspend fun fetchLogbookFromServerAndSaveIt()

    /**
     * Gets [year list][YearType] from local storage.
     * @return List<[YearType]>
     */
    suspend fun getYearList(): List<YearType>

    /**
     * Gets [YearMonthType] in order from local storage.
     * @return List<[YearMonthType]>
     */
    suspend fun getYearMonthTypeOrderedList(): List<YearMonthType>

    /**
     * Gets [YearMonth] from local storage.
     * @return List<[YearMonth]>
     */
    suspend fun getYearMonthList(): List<YearMonth>

    /**
     * Gets [MonthType] list for [year] from local storage.
     * @param year for which month list is taken
     * @return List<[MonthType]>
     */
    suspend fun getMonthTypeListByCurrentYear(year: Int): List<MonthType>

    /**
     * Gets [YearMonth] list from local storage.
     * @param yearOfPreviousMonth [Int] - year of (max - 1) month from local database,
     * @param previousMonth [Int] - (max - 1)  month from local database,
     * @return List<[YearMonth]>
     *     for period between params (year, month) until latest
     */
    suspend fun getYearAndMonthListFromPrevious(
        yearOfPreviousMonth: Int,
        previousMonth: Int
    ): List<YearMonth>

    /**
     * Gets summary [FlightTime] for all logbook items.
     * @return [FlightTime]
     */
    suspend fun getTotalTime(): FlightTime

    /**
     * Gets summary [FlightTime] for specific [year] and [month].
     * @param [year] to which month belongs
     * @param [month] for which time is calculated
     * @return [FlightTime]
     */
    suspend fun getTotalTimeByMonth(year: Int, month: Int): FlightTime

    /**
     * Gets summary [FlightTime] for specific [year].
     * @param [year] for which time is calculated
     * @return [FlightTime]
     */
    suspend fun getTotalTimeByYear(year: Int): FlightTime

    /**
     * Gets summary [FlightTime]
     * for all years until current [year] including from local storage.
     * @return [FlightTime]
     */
    suspend fun getTotalTimeWithYear(year: Int): FlightTime

    /**
     * Deletes all logbook data from local storage.
     */
    suspend fun deleteLogbook()
}