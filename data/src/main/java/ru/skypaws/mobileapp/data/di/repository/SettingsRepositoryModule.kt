package ru.skypaws.mobileapp.data.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.skypaws.mobileapp.data.repository.settings.AirportCodeSettingRepositoryImpl
import ru.skypaws.mobileapp.data.repository.settings.PathSettingRepositoryImpl
import ru.skypaws.mobileapp.data.repository.settings.ThemeSettingRepositoryImpl
import ru.skypaws.mobileapp.domain.repository.settings.AirportCodeSettingRepository
import ru.skypaws.mobileapp.domain.repository.settings.PathSettingRepository
import ru.skypaws.mobileapp.domain.repository.settings.ThemeSettingRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsRepositoryModule {
    @Binds
    abstract fun bindThemeSettingRepositoryImpl(
        themeSettingRepositoryImpl: ThemeSettingRepositoryImpl
    ): ThemeSettingRepository


    @Binds
    abstract fun bindAirportCodeSettingRepositoryImpl(
        airportCodeSettingRepositoryImpl: AirportCodeSettingRepositoryImpl
    ): AirportCodeSettingRepository

    @Binds
    abstract fun bindPathSettingRepository(
        pathSettingRepositoryImpl: PathSettingRepositoryImpl
    ): PathSettingRepository
}