package ru.skypaws.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.skypaws.data.source.entity.CrewPlanEventEntity

@Dao
interface CrewPlanDao {
    @Query("SELECT * FROM CrewPlanEventEntity ORDER BY dateTakeoff ASC")
    fun getCrewPlan(): Flow<List<CrewPlanEventEntity>>

    @Query("SELECT * FROM CrewPlanEventEntity")
    suspend fun getCurrentCrewPlan(): List<CrewPlanEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flight: List<CrewPlanEventEntity>)

    @Query("DELETE FROM CrewPlanEventEntity WHERE dateTakeoff == :date")
    suspend fun deleteOldEvents(date: String)

    @Query("DELETE FROM CrewPlanEventEntity")
    suspend fun deleteALl()
}