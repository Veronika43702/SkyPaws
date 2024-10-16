package ru.skypaws.data.storage

import android.net.Uri

interface SettingsStorage {
    fun getTheme(): Int
    fun saveTheme(newTheme: Int)

    fun getNewAirportCode(): Int
    fun saveNewAirportCode(newCode: Int)
    fun saveOldAirportCode(oldCode: Int)
    fun isNewCodeSet(): Boolean

    fun getPath(): String?
    fun savePath(uri: Uri)
}