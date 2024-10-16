package ru.skypaws.domain.repository


interface DownloadLogbookRepository {
    suspend fun downloadLogbook(): String
    fun isPathSet(): Boolean
    suspend fun saveLogbook(uriString: String, filename: String)
    suspend fun saveLogbookToChosenPath(filename: String)
}
