package ru.skypaws.mobileapp.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.repository.EnterRepositoryImpl
import ru.skypaws.mobileapp.domain.repository.EnterRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class EnterRepositoryModule {

    @Binds
    abstract fun bindEnterRepository(
        enterRepositoryImpl: EnterRepositoryImpl
    ): EnterRepository
}