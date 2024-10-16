package ru.skypaws.mobileapp.data.datasource.local.settings.impl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.skypaws.mobileapp.data.datasource.local.settings.AirportCodeSettingLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingGetLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settingsDataStore
import ru.skypaws.mobileapp.data.utils.keyAirportCode
import ru.skypaws.mobileapp.data.utils.keyNewAirportCode
import javax.inject.Inject

class AirportCodeSettingLocalDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingLocalDataSource<Int>, SettingGetLocalDataSource<Int>, AirportCodeSettingLocalDataSource {
    private val codeKey = intPreferencesKey(keyAirportCode)
    private val newCodeKey = intPreferencesKey(keyNewAirportCode)

    override val dataFlow: Flow<Int> =
        context.settingsDataStore.data.map { settings ->
            settings[newCodeKey] ?: 3
        }

    override suspend fun save(value: Int) {
        context.settingsDataStore.edit { settings ->
            settings[newCodeKey] = value
        }
    }

    override suspend fun get(): Int {
        val settings = context.settingsDataStore.data.first()
        return settings[newCodeKey] ?: 3
    }

    override suspend fun updateAirportCodeWithNewValue(newCode: Int) {
        context.settingsDataStore.edit { settings ->
            settings[codeKey] = newCode
        }
    }

    override suspend fun isNewCodeSet(): Boolean {
        val settings = context.settingsDataStore.data.first()
        val newCode: Int = settings[newCodeKey] ?: 3
        val code: Int = settings[codeKey] ?: 3
        return newCode != code
    }
}