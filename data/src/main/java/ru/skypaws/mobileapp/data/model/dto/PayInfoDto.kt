package ru.skypaws.mobileapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class PayInfoDto(
    val trial_until: Long,
    val logbook_exp: Long,
    val sync_exp: Long,
)