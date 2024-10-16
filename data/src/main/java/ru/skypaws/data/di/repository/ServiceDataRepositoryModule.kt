package ru.skypaws.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.data.repository.ServiceDataRepositoryImpl
import ru.skypaws.domain.repository.ServiceDataRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceDataRepositoryModule {

    @Binds
    abstract fun bindServiceDataRepository(
        serviceDataRepositoryImpl: ServiceDataRepositoryImpl
    ): ServiceDataRepository
}