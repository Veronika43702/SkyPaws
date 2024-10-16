package ru.skypaws.mobileapp.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.skypaws.mobileapp.data.datasource.db.dao.CrewPlanDao
import ru.skypaws.mobileapp.data.datasource.db.dao.LogbookDao
import ru.skypaws.mobileapp.data.datasource.db.dao.LogbookFlightDao
import ru.skypaws.mobileapp.data.model.entity.CrewPlanEventEntity
import ru.skypaws.mobileapp.data.model.entity.LogbookEntity
import ru.skypaws.mobileapp.data.model.entity.LogbookFlightEntity

@Database(
    entities = [CrewPlanEventEntity::class, LogbookEntity::class, LogbookFlightEntity::class],
    version = 1,
    exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun crewPlanDao(): CrewPlanDao
    abstract fun logbookDao(): LogbookDao
    abstract fun logbookFlightDao(): LogbookFlightDao
}