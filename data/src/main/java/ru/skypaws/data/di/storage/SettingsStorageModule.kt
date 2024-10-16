package ru.skypaws.data.di.storage

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.data.storage.SettingsStorage
import ru.skypaws.data.storage.SettingsStorageSharedPrefs

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsStorageModule {

    @Binds
    abstract fun bindSettingsStorage(settingsStorageSharedPrefs: SettingsStorageSharedPrefs): SettingsStorage
}