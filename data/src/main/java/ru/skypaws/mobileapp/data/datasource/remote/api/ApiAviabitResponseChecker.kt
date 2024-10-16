package ru.skypaws.mobileapp.data.datasource.remote.api

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import ru.skypaws.mobileapp.data.error.ApiErrors

object ApiAviabitResponseChecker {
    suspend fun <T> checkAviabitResponse(response: HttpResponse, body: suspend () -> T): T {
        if (!response.status.isSuccess()) {
            throw ApiErrors(response.status.value, response.status.description)
        }
        return try {
            body()
        } catch (e: Exception) {
            val content = response.bodyAsText()
            throw ApiErrors(0, "Unable to deserialize body: $content")
        }
    }
}