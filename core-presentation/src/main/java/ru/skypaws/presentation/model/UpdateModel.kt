package ru.skypaws.presentation.model

data class UpdateModel (
    val loading: Boolean = false,
    val downloading: Boolean = false,
    val downloaded: Boolean = false,

    // TODO ошибка скачиванимя не обработана
    val downloadError: Boolean = false,

    val downloadProgress: Float = 0f,

    val checkUpdatesFinished: Boolean = false,
    val newDB: Boolean = false,
    val newVersion: Boolean = false,
)