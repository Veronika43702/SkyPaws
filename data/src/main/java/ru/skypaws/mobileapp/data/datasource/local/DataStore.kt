package ru.skypaws.mobileapp.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import ru.skypaws.mobileapp.data.utils.localStorageSettings
import ru.skypaws.mobileapp.data.utils.localStorageUser

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = localStorageSettings,
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() }
)
val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = localStorageUser,
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() }
)