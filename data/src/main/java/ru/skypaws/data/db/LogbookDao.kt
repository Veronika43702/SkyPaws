package ru.skypaws.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.skypaws.data.source.entity.LogbookEntity
import ru.skypaws.domain.model.FlightTime
import ru.skypaws.domain.model.YearMonth
import ru.skypaws.domain.model.YearMonthType

@Dao
interface LogbookDao {
    @Query("SELECT * FROM LogbookEntity ORDER BY year DESC, month ASC")
    fun getLogbook(): List<LogbookEntity>

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
           END AS type,
           month AS month
    FROM LogbookEntity
    ORDER BY year DESC
""")
    suspend fun getYearList(): List<YearMonthType>

    @Query("SELECT year AS year, month AS month, type AS type FROM LogbookEntity WHERE year = :year ORDER BY year DESC, month ASC")
    suspend fun getMonthList(year: Int): List<YearMonthType>

    @Query("SELECT year AS year, month AS month, type AS type FROM LogbookEntity")
    suspend fun getAllMonth(): List<YearMonthType>

    @Query("SELECT year AS year, month AS month FROM LogbookEntity ORDER BY year DESC, month ASC")
    suspend fun getYearAndMonth(): List<YearMonth>

    @Query("SELECT MAX(month) FROM LogbookEntity WHERE year = (SELECT MAX(year) FROM LogbookEntity)")
    suspend fun getMaxMonthOfMaxYear(): Int

    @Query("SELECT MAX(year) FROM LogbookEntity")
    suspend fun getMaxYear(): Int

    @Query(
        "SELECT year AS year, month AS month FROM LogbookEntity " +
                "WHERE (year > :year OR (year = :year AND month >= :month)) " +
                "ORDER BY year DESC, month ASC"
    )
    suspend fun getYearsAndMonthsByCurrent(year: Int, month: Int): List<YearMonth>

    // Total
    @Query("SELECT SUM(timeFlight) AS flight, SUM(timeBlock) AS block, SUM(timeNight) AS night  FROM LogbookEntity WHERE type != 3")
    suspend fun sumTimeFlight(): FlightTime

    // By year
    @Query("SELECT SUM(timeFlight) AS flight, SUM(timeBlock) AS block, SUM(timeNight) AS night " +
            "FROM LogbookEntity WHERE year = :year")
    suspend fun sumTimeFlightByYear(year: Int): FlightTime

    // Total with the year
    @Query("SELECT SUM(timeFlight) AS flight, SUM(timeBlock) AS block, SUM(timeNight) AS night" +
            "  FROM LogbookEntity WHERE year <= :year")
    suspend fun sumTimeFlightWithYear(year: Int): FlightTime

    // By month
    @Query("SELECT SUM(timeFlight) AS flight, SUM(timeBlock) AS block, SUM(timeNight) AS night" +
            " FROM LogbookEntity WHERE year = :year AND type != 3 AND month = :month")
    suspend fun sumTimeFlightByMonth(year: Int, month: Int): FlightTime
}