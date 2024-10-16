package ru.skypaws.data.di.storage

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.data.storage.UserStorage
import ru.skypaws.data.storage.UserStorageSharedPrefs

@Module
@InstallIn(SingletonComponent::class)
abstract class UserStorageModule {

    @Binds
    abstract fun bindUserStorage(userStorageSharedPrefs: UserStorageSharedPrefs): UserStorage
}