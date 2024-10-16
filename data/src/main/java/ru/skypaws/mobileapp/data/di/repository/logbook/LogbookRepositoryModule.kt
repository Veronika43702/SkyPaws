package ru.skypaws.mobileapp.data.di.repository.logbook

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.repository.logbook.LogbookRepositoryImpl
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class LogbookRepositoryModule {

    @Binds
    abstract fun bindLogbookRepository(
        logbookRepositoryImpl: LogbookRepositoryImpl
    ): LogbookRepository
}