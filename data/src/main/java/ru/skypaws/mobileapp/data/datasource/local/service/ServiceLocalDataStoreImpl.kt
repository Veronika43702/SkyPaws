package ru.skypaws.mobileapp.data.datasource.local.service

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import ru.skypaws.mobileapp.data.datasource.local.settingsDataStore
import ru.skypaws.mobileapp.data.utils.keyLogbookPrice
import ru.skypaws.mobileapp.data.utils.keySyncMonthPrice
import ru.skypaws.mobileapp.data.utils.keySyncQuarterPrice
import ru.skypaws.mobileapp.data.utils.keySyncYearPrice
import javax.inject.Inject

class ServiceLocalDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ServiceLocalDataSource {

    override suspend fun savePriceInfo(
        logbookPrice: Int,
        syncMonthPrice: Int,
        syncQuarterPrice: Int,
        syncYearPrice: Int
    ) {
        context.settingsDataStore.edit { settings ->
            settings[intPreferencesKey(keyLogbookPrice)] = logbookPrice
            settings[intPreferencesKey(keySyncMonthPrice)] = syncMonthPrice
            settings[intPreferencesKey(keySyncQuarterPrice)] = syncQuarterPrice
            settings[intPreferencesKey(keySyncYearPrice)] = syncYearPrice
        }
    }

    override suspend fun getLogbookPrice(): Int {
        val settings = context.settingsDataStore.data.first()
        return settings[intPreferencesKey(keyLogbookPrice)] ?: 0
    }

    override suspend fun getCalendarMonthPrice(): Int {
        val settings = context.settingsDataStore.data.first()
        return settings[intPreferencesKey(keySyncMonthPrice)] ?: 0
    }

    override suspend fun getCalendarQuarterPrice(): Int {
        val settings = context.settingsDataStore.data.first()
        return settings[intPreferencesKey(keySyncQuarterPrice)] ?: 0
    }

    override suspend fun getCalendarYearPrice(): Int {
        val settings = context.settingsDataStore.data.first()
        return settings[intPreferencesKey(keySyncYearPrice)] ?: 0
    }
}