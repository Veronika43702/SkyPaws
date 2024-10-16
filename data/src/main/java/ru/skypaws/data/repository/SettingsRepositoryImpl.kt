package ru.skypaws.data.repository

import androidx.core.net.toUri
import ru.skypaws.data.storage.SettingsStorage
import ru.skypaws.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsStorageSharedPrefs: SettingsStorage
): SettingsRepository {

    override fun getTheme() = settingsStorageSharedPrefs.getTheme()
    override fun saveTheme(newTheme: Int) {
        settingsStorageSharedPrefs.saveTheme(newTheme)
    }

    override fun getNewAirportCode() = settingsStorageSharedPrefs.getNewAirportCode()
    override fun saveNewAirportCode(newCode: Int) {
        settingsStorageSharedPrefs.saveNewAirportCode(newCode)
    }
    override fun saveOldAirportCode(oldCode: Int) {
        settingsStorageSharedPrefs.saveOldAirportCode(oldCode)
    }

    override fun getPath(): String? = settingsStorageSharedPrefs.getPath()
    override fun savePath(uri: String) {
        settingsStorageSharedPrefs.savePath(uri.toUri())
    }
}
