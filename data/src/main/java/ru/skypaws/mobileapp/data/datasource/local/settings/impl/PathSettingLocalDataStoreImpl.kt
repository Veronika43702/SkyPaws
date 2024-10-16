package ru.skypaws.mobileapp.data.datasource.local.settings.impl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingGetLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settingsDataStore
import ru.skypaws.mobileapp.data.utils.keyPath
import javax.inject.Inject

class PathSettingLocalDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingLocalDataSource<String?>, SettingGetLocalDataSource<String?> {
    private val pathKey = stringPreferencesKey(keyPath)

    override val dataFlow: Flow<String?> =
        context.settingsDataStore.data.map { settings ->
            settings[pathKey]
        }

    override suspend fun save(value: String?) {
        context.settingsDataStore.edit { settings ->
            if (value != null) {
                settings[pathKey] = value
            } else settings.remove(pathKey)
        }
    }

    override suspend fun get(): String? {
        val settings = context.settingsDataStore.data.first()
        return settings[pathKey]
    }
}