package ru.skypaws.mobileapp.data.datasource.remote.api

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import ru.skypaws.mobileapp.data.error.ApiErrors

object ApiResponseChecker {
    suspend fun <T> checkResponse(response: HttpResponse, body: suspend () -> T): T {
        if (!response.status.isSuccess()) {
            throw ApiErrors(response.status.value, response.status.description)
        }
        return try {
          body()
        } catch (e: Exception) {
            val content = response.bodyAsText()
            throw ApiErrors(0, "Received not correct type content for ${response.call.request.url}: $content")
        }
    }
}