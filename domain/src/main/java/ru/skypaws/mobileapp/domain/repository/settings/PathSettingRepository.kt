package ru.skypaws.mobileapp.domain.repository.settings

import kotlinx.coroutines.flow.Flow

interface PathSettingRepository {
    /**
     * Gets path flow from local storage.
     * @return Flow<[String]?>
     */
    val path: Flow<String?>

    /**
     * Saves [uri] (path) in local storage.
     */
    suspend fun savePath(uri: String)

    /**
     * Gets path from local storage.
     * @return [String]?
     */
    suspend fun getPath(): String?
}
