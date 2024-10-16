package ru.skypaws.mobileapp.data.datasource.local.settings

import kotlinx.coroutines.flow.Flow

interface SettingLocalDataSource<T> {
    val dataFlow: Flow<T>

    suspend fun save(value: T)
}