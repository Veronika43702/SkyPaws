package ru.skypaws.mobileapp.domain.model

data class LogbookModelData(
    val listOfYear: List<YearType> = emptyList(),
    val totalBlock: String = "",
    val totalFlight: String = "",
    val totalNight: String = "",

    val totalFlightByMonth: Map<YearMonthType, String> = emptyMap(),
    val totalBlockByMonth: Map<YearMonthType, String> = emptyMap(),
    val totalNightByMonth: Map<YearMonthType, String> = emptyMap(),

    val flightBlock: Map<LogbookFlight, String> = emptyMap(),
    val flightFlight: Map<LogbookFlight, String> = emptyMap(),
    val flightNight: Map<LogbookFlight, String> = emptyMap(),
)
