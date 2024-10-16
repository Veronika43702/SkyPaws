package ru.skypaws.data.di.storage

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.data.storage.ServiceStorage
import ru.skypaws.data.storage.ServiceStorageSharedPrefs

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceStorageModule {

    @Binds
    abstract fun bindServiceStorage(serviceStorageSharedPrefs: ServiceStorageSharedPrefs): ServiceStorage
}