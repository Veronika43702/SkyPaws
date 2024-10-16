package ru.skypaws.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.data.repository.DownloadLogbookRepositoryImpl
import ru.skypaws.domain.repository.DownloadLogbookRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadRepositoryModule {

    @Binds
    abstract fun bindDownloadLogbookRepository(
        downloadLogbookRepositoryImpl: DownloadLogbookRepositoryImpl
    ): DownloadLogbookRepository
}