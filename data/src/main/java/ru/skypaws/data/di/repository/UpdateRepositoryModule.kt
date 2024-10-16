package ru.skypaws.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.data.repository.UpdateRepositoryImpl
import ru.skypaws.domain.repository.UpdateRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class UpdateRepositoryModule {

    @Binds
    abstract fun bindUpdateRepository(
        updateRepositoryImpl: UpdateRepositoryImpl
    ): UpdateRepository
}