package ru.skypaws.domain.repository

import ru.skypaws.domain.model.FlightTime
import ru.skypaws.domain.model.LogbookFlight
import ru.skypaws.domain.model.YearMonth
import ru.skypaws.domain.model.YearMonthType
import ru.skypaws.domain.model.YearType

interface LogbookRepository {
    suspend fun getYearList(): List<YearType>
    suspend fun getLogbook()
    suspend fun getLogbookItemDetailed(year: Int, month: Int)
    suspend fun getLogbookDetailed(): List<YearMonth>
    suspend fun getYearsAndMonthsByCurrent(year: Int, month: Int): List<YearMonth>
    suspend fun getLatestMonth(): Int
    suspend fun getLatestYear(): Int
    suspend fun getMonthList(year: Int): List<YearMonthType>
    suspend fun getAllMonth(): List<YearMonthType>
    suspend fun getAllFlights(): List<LogbookFlight>
    suspend fun getFlightListByYearMonth(year: Int, month: Int): List<LogbookFlight>
    suspend fun getTotalFlight(): FlightTime
    suspend fun getTotalFlightByYear(year: Int): FlightTime
    suspend fun getTotalFlightByMonth(year: Int, month: Int): FlightTime
    suspend fun getTotalFlightWithYear(year: Int): FlightTime
    suspend fun delete()
}