package ru.skypaws.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.data.repository.LogbookRepositoryImpl
import ru.skypaws.domain.repository.LogbookRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class LogbookRepositoryModule {

    @Binds
    abstract fun bindLogbookRepository(
        logbookRepositoryImpl: LogbookRepositoryImpl
    ): LogbookRepository
}