package ru.skypaws.mobileapp.data.di.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.datasource.db.AppDB
import ru.skypaws.mobileapp.data.datasource.db.dao.CrewPlanDao
import ru.skypaws.mobileapp.data.datasource.db.dao.LogbookDao
import ru.skypaws.mobileapp.data.datasource.db.dao.LogbookFlightDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDB(@ApplicationContext context: Context): AppDB {
        return Room.databaseBuilder(context, AppDB::class.java, "aviabit.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCrewPlanDao(appDB: AppDB): CrewPlanDao = appDB.crewPlanDao()

    @Provides
    fun provideLogbookDao(appDB: AppDB): LogbookDao = appDB.logbookDao()

    @Provides
    fun provideLogbookDetailedDao(appDB: AppDB): LogbookFlightDao = appDB.logbookFlightDao()
}