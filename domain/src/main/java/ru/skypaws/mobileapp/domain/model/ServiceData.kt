package ru.skypaws.mobileapp.domain.model

data class Updates(
    val version: String,
    val revision: Int,
)

data class Prices(
    val logbook: Int,
    val sync_month: Int,
    val sync_quarter: Int,
    val sync_year: Int,
)