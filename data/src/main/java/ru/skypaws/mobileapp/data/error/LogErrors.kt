package ru.skypaws.mobileapp.data.error

import kotlinx.serialization.Serializable
import java.io.PrintWriter
import java.io.StringWriter

@Serializable
data class LogErrors (
    val tag: String,
    val message: String,
) {
    companion object {
        fun fromException(tag: String, e: Exception): LogErrors {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            return LogErrors(tag, sw.toString())

        }
    }
}