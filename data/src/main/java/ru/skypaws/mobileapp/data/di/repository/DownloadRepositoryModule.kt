package ru.skypaws.mobileapp.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.repository.DownloadLogbookRepositoryImpl
import ru.skypaws.mobileapp.domain.repository.DownloadLogbookRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadRepositoryModule {

    @Binds
    abstract fun bindDownloadLogbookRepository(
        downloadLogbookRepositoryImpl: DownloadLogbookRepositoryImpl
    ): DownloadLogbookRepository
}