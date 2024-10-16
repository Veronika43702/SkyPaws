package ru.skypaws.data.api

import android.util.Log
import io.ktor.client.plugins.logging.Logger

class LoggingInterceptor : Logger {
    override fun log(message: String) {
        Log.d("Ktor", message)
    }
}