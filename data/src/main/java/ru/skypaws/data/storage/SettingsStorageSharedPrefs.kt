package ru.skypaws.data.storage

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.skypaws.data.utils.keyAirportCode
import ru.skypaws.data.utils.keyNewAirportCode
import ru.skypaws.data.utils.keyPath
import ru.skypaws.data.utils.keyTheme
import ru.skypaws.data.utils.prefSettings
import javax.inject.Inject

class SettingsStorageSharedPrefs @Inject constructor(
    @ApplicationContext private val context: Context
): SettingsStorage {
    private val prefsSettings = context.getSharedPreferences(prefSettings, Context.MODE_PRIVATE)

    override fun getTheme() = prefsSettings.getInt(keyTheme, 0)
    override fun saveTheme(newTheme: Int) {
        prefsSettings.edit()
            .putInt(keyTheme, newTheme)
            .apply()
    }

    override fun getNewAirportCode(): Int = prefsSettings.getInt(keyNewAirportCode, 3)
    override fun saveNewAirportCode(newCode: Int) {
        prefsSettings.edit()
            .putInt(keyNewAirportCode, newCode)
            .apply()
    }
    override fun saveOldAirportCode(oldCode: Int) {
        prefsSettings.edit()
            .putInt(keyAirportCode, oldCode)
            .apply()
    }
    override fun isNewCodeSet(): Boolean =
        (prefsSettings.getInt(keyNewAirportCode, 3) != prefsSettings.getInt(keyAirportCode, 3))

    override fun getPath() = prefsSettings.getString(keyPath, null)
    override fun savePath(uri: Uri) {
        prefsSettings.edit()
            .putString(keyPath, uri.toString())
            .apply()
    }
}