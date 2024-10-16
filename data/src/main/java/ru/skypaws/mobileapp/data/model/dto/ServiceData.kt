package ru.skypaws.mobileapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdatesDto(
    val version: String,
    val revision: Int,
)

@Serializable
data class PricesDto(
    val logbook: Int,
    val sync_month: Int,
    val sync_quarter: Int,
    val sync_year: Int,
)