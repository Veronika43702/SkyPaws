package ru.skypaws.mobileapp.data.di.repository.logbook

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.repository.logbook.LogbookFlightRepositoryImpl
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookFlightRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class LogbookFlightRepositoryModule {

    @Binds
    abstract fun bindLogbookFlightRepository(
        logbookFlightRepositoryImpl: LogbookFlightRepositoryImpl
    ): LogbookFlightRepository
}