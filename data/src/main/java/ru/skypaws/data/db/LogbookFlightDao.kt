package ru.skypaws.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.skypaws.data.source.entity.LogbookFlightEntity

@Dao
interface LogbookFlightDao {
    @Query("SELECT * FROM LogbookFlightEntity WHERE year = :year AND month = :month ORDER BY dateFlight ASC")
    suspend fun getFlightList(year: Int, month: Int): List<LogbookFlightEntity>

    @Query("SELECT * FROM LogbookFlightEntity ORDER BY dateFlight ASC")
    suspend fun getAllFlights(): List<LogbookFlightEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogbookDetailed(logbookItem: List<LogbookFlightEntity>)

    @Query("DELETE FROM LogbookFlightEntity WHERE year = :year AND month = :month")
    suspend fun deleteLogbookItem(year: Int, month: Int)

    @Query("DELETE FROM LogbookFlightEntity")
    suspend fun deleteLogbookDetailed()
}