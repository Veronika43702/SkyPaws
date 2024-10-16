package ru.skypaws.mobileapp.data.di.repository.logbook

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.repository.logbook.LogbookAndFlightRepositoryImpl
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookAndFlightRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class LogbookAndFlightRepositoryModule {

    @Binds
    abstract fun bindLogbookAndFlightRepository(
        logbookAndFlightRepositoryImpl: LogbookAndFlightRepositoryImpl
    ): LogbookAndFlightRepository
}