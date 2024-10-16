package ru.skypaws.features.model

data class PaidServicesState(
    val isLogbookPaid: Boolean = false,
    val logbookPrice: Int = 0,
    val logbookExpDate: String? = null,

    val isCalendarPaid: Boolean = false,
    val calendarMonthPrice: Int = 0,
    val calendarQuarterPrice: Int = 0,
    val calendarYearPrice: Int = 0,
    val calendarExpDate: String? = null,

    val networkError: Boolean = false,
    val refreshing: Boolean = false,
)