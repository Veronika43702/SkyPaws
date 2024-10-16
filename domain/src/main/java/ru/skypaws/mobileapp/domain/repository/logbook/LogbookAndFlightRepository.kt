package ru.skypaws.mobileapp.domain.repository.logbook

import ru.skypaws.mobileapp.domain.model.FlightTime
import ru.skypaws.mobileapp.domain.model.LogbookFlight
import ru.skypaws.mobileapp.domain.model.LogbookModelData
import ru.skypaws.mobileapp.domain.model.MonthType
import ru.skypaws.mobileapp.domain.model.YearMonth
import ru.skypaws.mobileapp.domain.model.YearMonthType

interface LogbookAndFlightRepository {
    /**
     * Fetches logbook and flight data from server and saves it.
    */
    suspend fun fetchLogbookAndFlightsFromServer()

    /**
     * Fetches logbook data and updates or fetches all flight data from server and saves it.
     */
    suspend fun fetchAllOrUpdateLogbookAndFlightsFromServer()

    /**
     * Gets all logbook and flight data from local storage.
     * @return [LogbookModelData]
     */
    suspend fun getAllLogbookData(): LogbookModelData

    /**
     * Gets from local storage and calculate [FlightTime] for each month in [monthList].
     * @return [LogbookModelData] with updated data.
     */
    suspend fun getTimeAndCalculateByMonth(monthList: List<YearMonthType>): LogbookModelData

    /**
     * Calculates [FlightTime] for each flight in [flights].
     * @return [LogbookModelData] with updated data.
     */
    fun calculateTimeForFlights(flights: List<LogbookFlight>): LogbookModelData

    /**
     * Calculates total [FlightTime].
     * @return [LogbookModelData]
     */
    fun calculateTotalTime(totalTime: FlightTime): LogbookModelData


    /**
     * Gets [MonthType] list for [year].
     * @param year for which month list is taken
     * @return List<[MonthType]>
     */
    suspend fun getMonthTypeListByCurrentYear(year: Int): List<MonthType>

    /**
     * Gets [LogbookFlight] list for [yearMonth].
     * @param yearMonth for which flight are taken.
     * @return List<[LogbookFlight]>
     */
    suspend fun getFlightListByYearMonth(yearMonth: YearMonth): List<LogbookFlight>

    /**
     * Gets summary [FlightTime] for specific [year].
     * @param [year] for which time is calculated
     * @return [FlightTime]
     */
    suspend fun getTotalTimeByYear(year: Int): FlightTime

    /**
     * Gets summary [FlightTime] for all years until current [year] including.
     * @return [FlightTime]
     */
    suspend fun getTotalTimeWithYear(year: Int): FlightTime

    /**
     * Deletes all items logbook and flight data from local storage.
     */
    suspend fun deleteLogbookAndFlights()
}