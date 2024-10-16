package ru.skypaws.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.data.repository.EnterRepositoryImpl
import ru.skypaws.domain.repository.EnterRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class EnterRepositoryModule {

    @Binds
    abstract fun bindEnterRepository(
        enterRepositoryImpl: EnterRepositoryImpl
    ): EnterRepository
}