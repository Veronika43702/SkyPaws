package ru.skypaws.data.utils

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class DateUtilsImpl @Inject constructor() : DateUtils {

    override fun getDate(seconds: Long): String {
        return SimpleDateFormat("dd.MM.yyyy", Locale(UTC.toString())).format(seconds * 1000L)
    }

    override fun getTime(seconds: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date(seconds * 1000L))
    }

    override fun getTimeFromISO(dateTime: String?): String {
        if (dateTime != null) {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

            val zonedDateTime = ZonedDateTime.parse(dateTime, inputFormatter)

            return timeFormatter.format(zonedDateTime).toString()
        } else return ""
    }

    override fun getDateFromISO(dateTime: String?): String {
        if (dateTime != null) {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

            val zonedDateTime = ZonedDateTime.parse(dateTime, inputFormatter)

            return dateFormatter.format(zonedDateTime).toString()
        } else return ""
    }

    /**
     * Transforms ISO date type to day of week (number) type.
     * @param [dateTime] ISO date type
     * @return [Int] [1..7] or 100 if [dateTime] is null
     */
    override fun getDateNumberFromISO(dateTime: String?): String {
        if (dateTime != null) {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            val dateFormatter = DateTimeFormatter.ofPattern("dd")

            val zonedDateTime = ZonedDateTime.parse(dateTime, inputFormatter)

            return dateFormatter.format(zonedDateTime).toString()
        } else return ""
    }

    override fun getTimeBetween(dateFrom: String?, dateUntil: String?): String {
        if (dateFrom != null && dateUntil != null) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")

            val zonedDateTimeFrom = ZonedDateTime.parse(dateFrom, formatter)
            val zonedDateTimeUntil = ZonedDateTime.parse(dateUntil, formatter)

            val duration = Duration.between(zonedDateTimeFrom, zonedDateTimeUntil)

            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60

            return String.format(Locale(UTC.toString()), "%02d:%02d", hours, minutes)
        } else return ""
    }

    override fun getTimeLandingCalculated(
        date: String?,
        dateFrom: String?,
        dateUntil: String?
    ): String {
        if (date != null && dateFrom != null && dateUntil != null) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

            val zonedDateTimeFrom = ZonedDateTime.parse(dateFrom, formatter)
            val zonedDateTimeUntil = ZonedDateTime.parse(dateUntil, formatter)
            val zonedDate = ZonedDateTime.parse(date, formatter)

            val duration = Duration.between(zonedDateTimeFrom, zonedDateTimeUntil)
            val timeLandingCalculated = zonedDate.plus(duration)

            return timeLandingCalculated.format(timeFormatter)
        } else return ""
    }

    override fun getISOTimeLandingCalculated(
        date: String?,
        dateFrom: String?,
        dateUntil: String?
    ): String {
        if (date != null && dateFrom != null && dateUntil != null) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")

            val zonedDateTimeFrom = ZonedDateTime.parse(dateFrom, formatter)
            val zonedDateTimeUntil = ZonedDateTime.parse(dateUntil, formatter)
            val zonedDate = ZonedDateTime.parse(date, formatter)

            val duration = Duration.between(zonedDateTimeFrom, zonedDateTimeUntil)
            val timeLandingCalculated = zonedDate.plus(duration)

            return timeLandingCalculated.format(formatter)
        } else return ""
    }

    override fun addMinutesToTime(date: String?, minutes: Long): String {
        if (date != null) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            val zonedDateTime = ZonedDateTime.parse(date, formatter)

            val totalTime = zonedDateTime.plusMinutes(minutes)

            return totalTime.format(formatter)
        } else return ""
    }

    /**
     * Transforms ISO date type to day of week (number) type.
     * @param [dateTime] ISO date type
     * @return [Int] [1..7] or 100 if [dateTime] is null
     */
    override fun getDayOfWeek(dateTime: String?): Int {
        if (dateTime != null) {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            val zonedDateTime = ZonedDateTime.parse(dateTime, inputFormatter)
            return zonedDateTime.dayOfWeek.value
        } else return 100
    }
}