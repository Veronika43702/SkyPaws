package ru.skypaws.domain.repository


interface SettingsRepository {
    fun getTheme(): Int
    fun saveTheme(newTheme: Int)

    fun getNewAirportCode(): Int
    fun saveNewAirportCode(newCode: Int)
    fun saveOldAirportCode(oldCode: Int)

    fun getPath(): String?
    fun savePath(uri: String)
}
