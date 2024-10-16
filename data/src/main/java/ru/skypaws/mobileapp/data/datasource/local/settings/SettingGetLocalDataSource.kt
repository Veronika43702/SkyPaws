package ru.skypaws.mobileapp.data.datasource.local.settings

interface SettingGetLocalDataSource<T> {
    suspend fun get(): T
}