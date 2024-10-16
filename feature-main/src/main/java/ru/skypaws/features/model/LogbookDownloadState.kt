package ru.skypaws.features.model

data class LogbookDownloadState(
    val logbookExpDate: String = "",

    val downloading: Boolean = false,
    val downloadedTemp: Boolean = false,
    val downloaded: Boolean = false,
    val downloadError: Boolean = false,

    val savingError: Boolean = false,
    val isPathSet: Boolean = false,
    val filename: String = "logbook.pdf",
    val path: String? = null
)