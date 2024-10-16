package ru.skypaws.data.api

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import ru.skypaws.data.error.ApiErrors

object ApiAviabitResponseChecker {
    suspend fun <T> checkAviabitResponse(response: HttpResponse, body: suspend () -> T): T {
        if (!response.status.isSuccess()) {
            throw ApiErrors(response.status.value, response.status.description)
        }
        return try {
            body()
        } catch (e: Exception) {
            val content = response.bodyAsText()
            throw ApiErrors(0, "Received not Json content: $content")
        }
    }
}