package ru.skypaws.mobileapp.domain.repository


interface DownloadLogbookRepository {
    /**
     * Fetches logbook from server and saves file.
     */
    suspend fun downloadLogbook(): String

    /**
     * Checks presence of path in local storage.
     */
    suspend fun isPathSet(): Boolean

    /**
     * Saves file (copies file from temporary URI) to the [uriString] with [filename] name.
     */
    suspend fun saveLogbook(uriString: String, filename: String)

    /**
     * Saves file (copies file from temporary URI)
     * with [filename] name to chosen path (saved locally).
     */
    suspend fun saveLogbookToChosenPath(filename: String)
}
