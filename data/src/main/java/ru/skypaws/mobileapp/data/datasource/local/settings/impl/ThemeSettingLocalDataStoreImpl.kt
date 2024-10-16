package ru.skypaws.mobileapp.data.datasource.local.settings.impl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.skypaws.mobileapp.data.datasource.local.settings.SettingLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.settingsDataStore
import ru.skypaws.mobileapp.data.utils.keyTheme
import javax.inject.Inject

class ThemeSettingLocalDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingLocalDataSource<Int> {
    private val themeKey = intPreferencesKey(keyTheme)

    override val dataFlow: Flow<Int> =
        context.settingsDataStore.data.map { settings ->
            settings[themeKey] ?: 0
        }

    override suspend fun save(value: Int) {
        context.settingsDataStore.edit { settings ->
            settings[themeKey] = value
        }
    }
}