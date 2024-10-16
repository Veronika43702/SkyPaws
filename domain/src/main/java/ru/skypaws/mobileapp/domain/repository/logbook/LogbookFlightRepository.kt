package ru.skypaws.mobileapp.domain.repository.logbook

import ru.skypaws.mobileapp.domain.model.LogbookFlight
import ru.skypaws.mobileapp.domain.model.YearMonth

interface LogbookFlightRepository {
    /**
     * Fetches [LogbookFlight] from remoteDataSource
     * for specific [year] and [month] and saves it locally.
     */
    suspend fun fetchLogbookFlightsFromServerAndSaveIt(year: Int, month: Int)
    
    /**
     * Gets all flights from local storage.
     * @return List<[LogbookFlight]>
     */
    suspend fun getAllFlightsList(): List<LogbookFlight>

    /**
     * Gets [LogbookFlight] list for specific [yearMonth]
     * from local storage.
     * @param yearMonth year and month for which data is taken.
     * @return List<[LogbookFlight]>
     */
    suspend fun getFlightListByYearMonth(yearMonth: YearMonth): List<LogbookFlight>

    /**
     * Gets latest year and month [YearMonth][ru.skypaws.mobileapp.model.YearMonth]
     * from local storage.
     */
    suspend fun getLatestYearMonth(): YearMonth

    /**
     * Saves [LogbookFlight] data locally.
     * @param flights List<[LogbookFlight]>
     */
    suspend fun saveLogbookFlight(flights: List<LogbookFlight>)

    /**
     * Deletes [LogbookFlight] data corresponding to [year] and [month] from local storage.
     * @param year year for which data must be deleted
     * @param month month for which data must be deleted
     */
    suspend fun deleteLogbookFlightByYearMonth(year: Int, month: Int)

    /**
     * Deletes all logbook flight data from local storage.
     */
    suspend fun deleteLogbookFlights()
}