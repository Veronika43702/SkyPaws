package ru.skypaws.mobileapp.data.datasource.remote.api

import android.util.Log
import io.ktor.client.plugins.logging.Logger

class LoggingInterceptor : Logger {
    override fun log(message: String) {
        Log.d("Ktor", message)
    }
}