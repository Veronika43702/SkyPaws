package ru.skypaws.mobileapp.data.datasource.local.user.impl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.skypaws.mobileapp.data.datasource.local.user.UserPayInfoLocalDataSource
import ru.skypaws.mobileapp.data.datasource.local.userDataStore
import ru.skypaws.mobileapp.data.model.dto.PayInfoDto
import ru.skypaws.mobileapp.data.utils.keyLogbookExpDate
import ru.skypaws.mobileapp.data.utils.keySyncExpDate
import ru.skypaws.mobileapp.data.utils.keyTrialUntil
import javax.inject.Inject

class UserPayInfoLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPayInfoLocalDataSource {
    private val trialUntil = longPreferencesKey(keyTrialUntil)
    private val logbookExpDate = longPreferencesKey(keyLogbookExpDate)
    private val syncExpDate = longPreferencesKey(keySyncExpDate)

    /**
     * saves user [PayInfoDto] in local storage
     */
    override suspend fun savePayInfo(payInfoDto: PayInfoDto) {
        context.userDataStore.edit { preferences ->
            preferences[trialUntil] = payInfoDto.trial_until
            preferences[logbookExpDate] = payInfoDto.logbook_exp
            preferences[syncExpDate] = payInfoDto.sync_exp
        }
    }

    /**
     * gets Logbook Expiration Date from local storage
     */
    override suspend fun getLogbookExpDate(): Long =
        context.userDataStore.data.map { preferences ->
            preferences[logbookExpDate] ?: 0L
        }.first()

    /**
     * gets Calendar Expiration Date from local storage
     */
    override suspend fun getCalendarExpDate(): Long =
        context.userDataStore.data.map { preferences ->
            preferences[syncExpDate] ?: 0L
        }.first()
}