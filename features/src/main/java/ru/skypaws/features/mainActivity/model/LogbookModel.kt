package ru.skypaws.features.mainActivity.model

import ru.skypaws.domain.model.LogbookFlight
import ru.skypaws.domain.model.YearMonthType
import ru.skypaws.domain.model.YearType


data class LogbookModel(
    val listOfYear: List<YearType> = emptyList(),
    val monthList: Map<Int, List<YearMonthType>> = emptyMap(),
    val flightList: Map<YearMonthType, List<LogbookFlight>> = emptyMap(),

    val totalBlock: String = "",
    val totalFlight: String = "",
    val totalNight: String = "",

    val totalFlightByYear: Map<Int, String> = emptyMap(),
    val totalBlockByYear: Map<Int, String> = emptyMap(),
    val totalNightByYear: Map<Int, String> = emptyMap(),

    val totalFlightByMonth: Map<YearMonthType, String> = emptyMap(),
    val totalBlockByMonth: Map<YearMonthType, String> = emptyMap(),
    val totalNightByMonth: Map<YearMonthType, String> = emptyMap(),

    val totalFlightWithYear: Map<Int, String> = emptyMap(),
    val totalBlockWithYear: Map<Int, String> = emptyMap(),
    val totalNightWithYear: Map<Int, String> = emptyMap(),

    val flightBlock: Map<LogbookFlight, String> = emptyMap(),
    val flightFlight: Map<LogbookFlight, String> = emptyMap(),
    val flightNight: Map<LogbookFlight, String> = emptyMap(),
)
