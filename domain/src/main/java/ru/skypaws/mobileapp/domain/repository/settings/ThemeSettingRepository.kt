package ru.skypaws.mobileapp.domain.repository.settings

import kotlinx.coroutines.flow.Flow

interface ThemeSettingRepository {
    /**
     * Gets theme flow from local storage.
     * @return Flow<[Int]>
     */
    val theme: Flow<Int>

    /**
     * Saves [newTheme] in local storage.
     */
    suspend fun saveTheme(newTheme: Int)
}
