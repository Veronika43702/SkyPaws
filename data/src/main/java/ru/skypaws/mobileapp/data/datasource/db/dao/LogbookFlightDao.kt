package ru.skypaws.mobileapp.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.skypaws.mobileapp.data.model.entity.LogbookFlightEntity
import ru.skypaws.mobileapp.domain.model.YearMonth

@Dao
interface LogbookFlightDao {
    @Query("SELECT * FROM LogbookFlightEntity WHERE year = :year AND month = :month ORDER BY dateFlight ASC")
    suspend fun getFlightList(year: Int, month: Int): List<LogbookFlightEntity>

    @Query("SELECT * FROM LogbookFlightEntity ORDER BY dateFlight ASC")
    suspend fun getAllFlights(): List<LogbookFlightEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogbookFlights(logbookItem: List<LogbookFlightEntity>)

    @Query("DELETE FROM LogbookFlightEntity WHERE year = :year AND month = :month")
    suspend fun deleteLogbookItem(year: Int, month: Int)

    @Query("DELETE FROM LogbookFlightEntity")
    suspend fun deleteAllLogbookFlights()

    @Query("""
    SELECT
        CASE
            WHEN (SELECT COUNT(*) FROM LogbookFlightEntity) > 1 THEN (
                SELECT MAX(year)
                FROM LogbookFlightEntity
            )
            ELSE 0
        END AS year,
        CASE
            WHEN (SELECT COUNT(*) FROM LogbookFlightEntity) > 1 THEN (
                SELECT MAX(month)
                FROM LogbookFlightEntity
                WHERE year = (SELECT MAX(year) FROM LogbookFlightEntity)
            )
            ELSE 0
        END AS month
    FROM LogbookFlightEntity
    LIMIT 1
    """)
    suspend fun getMaxYearMonth(): YearMonth
}