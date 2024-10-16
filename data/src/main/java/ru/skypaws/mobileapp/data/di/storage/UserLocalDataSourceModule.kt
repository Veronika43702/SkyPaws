package ru.skypaws.mobileapp.data.di.storage

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.datasource.local.user.UserLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.user.impl.UserLocalDataSourceImpl
import ru.skypaws.mobileapp.data.datasource.local.user.UserPayInfoLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.user.impl.UserPayInfoLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UserLocalDataSourceModule {

    @Binds
    abstract fun bindUseDataSource(userStorageSharedPrefs: UserLocalDataSourceImpl): UserLocalDataSource

    @Binds
    abstract fun bindUserPayInfoDataSource(userPayInfoLocalDataSource: UserPayInfoLocalDataSourceImpl): UserPayInfoLocalDataSource
}