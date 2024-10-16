package ru.skypaws.mobileapp.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.skypaws.mobileapp.data.model.entity.LogbookEntity
import ru.skypaws.mobileapp.domain.model.FlightTime
import ru.skypaws.mobileapp.domain.model.MonthType
import ru.skypaws.mobileapp.domain.model.YearMonth
import ru.skypaws.mobileapp.domain.model.YearMonthType
import ru.skypaws.mobileapp.domain.model.YearType

@Dao
interface LogbookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogbook(logbookItem: List<LogbookEntity>)

    @Query("DELETE FROM LogbookEntity")
    suspend fun deleteLogbook()

    @Query("""
    SELECT DISTINCT year,
           CASE
              WHEN EXISTS (
                   SELECT 1
                   FROM LogbookEntity AS sub
                   WHERE sub.year = LogbookEntity.year AND sub.type != 0
               ) THEN 3
               ELSE 0
           END AS type
    FROM LogbookEntity
    ORDER BY year DESC
    """)
    suspend fun getYearList(): List<YearType>

    @Query("SELECT year AS year, month AS month, type AS type FROM LogbookEntity ORDER BY year DESC, month ASC")
    suspend fun getYearMonthTypeOrderedList(): List<YearMonthType>

    @Query("SELECT year AS year, month AS month FROM LogbookEntity ORDER BY year DESC, month ASC")
    suspend fun getYearMonthList(): List<YearMonth>

    @Query(
        "SELECT year AS year, month AS month FROM LogbookEntity " +
                "WHERE (year > :year OR (year = :year AND month >= :month)) " +
                "ORDER BY year DESC, month ASC"
    )
    suspend fun getYearsAndMonthsFromPrevious(year: Int, month: Int): List<YearMonth>

    @Query("SELECT month AS month, type AS type FROM LogbookEntity WHERE year = :year ORDER BY year DESC, month ASC")
    suspend fun getMonthListByCurrentYear(year: Int): List<MonthType>

    // Total
    @Query("SELECT SUM(timeFlight) AS flight, SUM(timeBlock) AS block, SUM(timeNight) AS night  FROM LogbookEntity WHERE type != 3")
    suspend fun sumTimeFlight(): FlightTime

    // By month
    @Query("SELECT SUM(timeFlight) AS flight, SUM(timeBlock) AS block, SUM(timeNight) AS night" +
            " FROM LogbookEntity WHERE year = :year AND type != 3 AND month = :month")
    suspend fun sumTimeFlightByMonth(year: Int, month: Int): FlightTime

    // By year
    @Query("SELECT SUM(timeFlight) AS flight, SUM(timeBlock) AS block, SUM(timeNight) AS night " +
            "FROM LogbookEntity WHERE year = :year")
    suspend fun sumTimeFlightByYear(year: Int): FlightTime

    // Total with the year
    @Query("SELECT SUM(timeFlight) AS flight, SUM(timeBlock) AS block, SUM(timeNight) AS night" +
            "  FROM LogbookEntity WHERE year <= :year")
    suspend fun sumTimeFlightWithYear(year: Int): FlightTime
}