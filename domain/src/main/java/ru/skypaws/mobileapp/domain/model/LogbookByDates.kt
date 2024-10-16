package ru.skypaws.mobileapp.domain.model

data class YearMonthType(
    val year: Int,
    val month: Int,
    val type: Int
)

data class YearMonth(
    val year: Int,
    val month: Int
)

data class MonthType(
    val month: Int,
    val type: Int
)

data class YearType(
    val year: Int,
    val type: Int
)

data class FlightTime(
    val block: Int,
    val flight: Int,
    val night: Int
)