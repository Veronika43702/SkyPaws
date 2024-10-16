package ru.skypaws.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.skypaws.data.source.entity.CrewPlanEventEntity
import ru.skypaws.data.source.entity.LogbookEntity
import ru.skypaws.data.source.entity.LogbookFlightEntity

@Database(
    entities = [CrewPlanEventEntity::class, LogbookEntity::class, LogbookFlightEntity::class],
    version = 1,
    exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun crewPlanDao(): CrewPlanDao
    abstract fun logbookDao(): LogbookDao
    abstract fun logbookFlightDao(): LogbookFlightDao
}