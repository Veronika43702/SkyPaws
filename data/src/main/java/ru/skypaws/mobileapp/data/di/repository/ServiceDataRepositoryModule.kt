package ru.skypaws.mobileapp.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.repository.ServiceDataRepositoryImpl
import ru.skypaws.mobileapp.domain.repository.ServiceDataRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceDataRepositoryModule {

    @Binds
    abstract fun bindServiceDataRepository(
        serviceDataRepositoryImpl: ServiceDataRepositoryImpl
    ): ServiceDataRepository
}