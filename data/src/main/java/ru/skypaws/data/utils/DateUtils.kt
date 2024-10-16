package ru.skypaws.data.utils

interface DateUtils {
    fun getDate(seconds: Long): String
    fun getTime(seconds: Long): String
    fun getTimeFromISO(dateTime: String?): String
    fun getDateFromISO(dateTime: String?): String
    fun getDateNumberFromISO(dateTime: String?): String
    fun getTimeBetween(dateFrom: String?, dateUntil: String?): String
    fun getTimeLandingCalculated(date: String?, dateFrom: String?, dateUntil: String?): String
    fun getISOTimeLandingCalculated(date: String?, dateFrom: String?, dateUntil: String?): String
    fun addMinutesToTime(date: String?, minutes: Long): String
    fun getDayOfWeek(dateTime: String?): Int
}