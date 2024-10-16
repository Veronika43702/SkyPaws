package ru.skypaws.mobileapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class LogbookDto(
    val type: Int = 0,
    val year: Int =0,
    val month: Int =0,
    val timeFlight: Int = 0,
    val timeBlock: Int = 0,
    val timeNight: Int = 0,
    val timeBiologicalNight: Int = 0,
    val timeWork: Int = 0
)