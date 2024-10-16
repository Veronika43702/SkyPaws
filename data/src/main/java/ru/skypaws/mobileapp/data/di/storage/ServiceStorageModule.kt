package ru.skypaws.mobileapp.data.di.storage

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.datasource.local.service.ServiceLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.service.ServiceLocalDataStoreImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceStorageModule {

    @Binds
    abstract fun bindServiceStorage(serviceLocalDataStoreImpl: ServiceLocalDataStoreImpl): ServiceLocalDataSource
}