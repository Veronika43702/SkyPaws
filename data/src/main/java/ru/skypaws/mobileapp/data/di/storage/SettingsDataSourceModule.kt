package ru.skypaws.mobileapp.data.di.storage

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.datasource.local.settings.AirportCodeSettingLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingGetLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settings.impl.AirportCodeSettingLocalDataStoreImpl
import ru.skypaws.mobileapp.data.datasource.local.settings.impl.PathSettingLocalDataStoreImpl
import ru.skypaws.mobileapp.data.datasource.local.settings.impl.ThemeSettingLocalDataStoreImpl
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ThemeSetting

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AirportCodeSetting

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PathSetting

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsDataSourceModule {
    @Binds
    @Singleton
    @ThemeSetting
    abstract fun bindThemeSettingLocalDataStore(themeSettingLocalDataStoreImpl: ThemeSettingLocalDataStoreImpl): SettingLocalDataSource<Int>

    @Binds
    @Singleton
    @AirportCodeSetting
    abstract fun bindAirportCodeSettingLocalDataStoreImpl(
        airportCodeSettingLocalDataStoreImpl: AirportCodeSettingLocalDataStoreImpl
    ): SettingLocalDataSource<Int>

    @Binds
    @Singleton
    @AirportCodeSetting
    abstract fun bindAirportCodeSettingGetLocalDataStoreImpl(
        airportCodeSettingLocalDataStoreImpl: AirportCodeSettingLocalDataStoreImpl
    ): SettingGetLocalDataSource<Int>

    @Binds
    @Singleton
    abstract fun bindAirportCodeSettingLocalDataStore(
        airportCodeSettingLocalDataStoreImpl: AirportCodeSettingLocalDataStoreImpl
    ): AirportCodeSettingLocalDataSource

    @Binds
    @Singleton
    @PathSetting
    abstract fun bindPathSettingStorage(
        pathSettingLocalDataStoreImpl: PathSettingLocalDataStoreImpl
    ): SettingLocalDataSource<String?>

    @Binds
    @Singleton
    @PathSetting
    abstract fun bindPathGetSettingStorage(
        pathSettingLocalDataStoreImpl: PathSettingLocalDataStoreImpl
    ): SettingGetLocalDataSource<String?>


}