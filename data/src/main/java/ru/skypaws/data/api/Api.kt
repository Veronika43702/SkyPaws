package ru.skypaws.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import ru.skypaws.data.error.LogErrors
import ru.skypaws.data.source.dto.PayInfoDto
import ru.skypaws.data.source.dto.PricesDto
import ru.skypaws.data.source.dto.UpdatesDto
import ru.skypaws.data.source.dto.UserDto

class ApiService(private val client: HttpClient) {

    suspend fun checkUpdates(): UpdatesDto {
        val response: HttpResponse = client.get("app/version") {
            contentType(ContentType.Application.Json)
        }
        return ApiResponseChecker.checkAviabitResponse(response) {
            response.body<UpdatesDto>()
        }
    }

    suspend fun downloadApk(): HttpResponse {
        return client.get("app") {
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun auth(username: String, password: String): UserDto {
        val response: HttpResponse = client.post("signin") {
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("username", JsonPrimitive(username))
                put("password", JsonPrimitive(password))
            })
        }
        return ApiResponseChecker.checkAviabitResponse(response) {
            response.body<UserDto>()
        }
    }

    suspend fun sendCode(username: String,
                         password: String,
                         airline: Int): UserDto {
        val response: HttpResponse = client.post("signup") {
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("username", JsonPrimitive(username))
                put("password", JsonPrimitive(password))
                put("airline", JsonPrimitive(airline))
            })
        }
        return ApiResponseChecker.checkAviabitResponse(response) {
            response.body<UserDto>()
        }
    }

    suspend fun register(id: String,
                         username: String,
                         password: String,
                         code: String): UserDto {
        val response: HttpResponse = client.post("verify") {
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("id", JsonPrimitive(id))
                put("username", JsonPrimitive(username))
                put("password", JsonPrimitive(password))
                put("code", JsonPrimitive(code))
            })
        }
        return ApiResponseChecker.checkAviabitResponse(response) {
            response.body<UserDto>()
        }
    }

    suspend fun getUser(): UserDto {
        val response: HttpResponse = client.get("users/me") {
            contentType(ContentType.Application.Json)
        }
        return ApiResponseChecker.checkAviabitResponse(response) {
            response.body<UserDto>()
        }
    }

    suspend fun getPayInfo(): PayInfoDto {
        val response: HttpResponse = client.get("users/payments") {
            contentType(ContentType.Application.Json)
        }
        return ApiResponseChecker.checkAviabitResponse(response) {
            response.body<PayInfoDto>()
        }
    }

    suspend fun getPriceInfo(): PricesDto {
        val response: HttpResponse = client.get("price") {
            contentType(ContentType.Application.Json)
        }
        return ApiResponseChecker.checkAviabitResponse(response) {
            response.body<PricesDto>()
        }
    }

    suspend fun downloadLogbook(): HttpResponse {
        return client.get("") {
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun sendError(log: LogErrors): HttpResponse {
        return client.post("a") {
            contentType(ContentType.Application.Json)
            setBody(log)
        }
    }
}