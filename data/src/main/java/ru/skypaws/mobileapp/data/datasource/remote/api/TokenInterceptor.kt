package ru.skypaws.mobileapp.data.datasource.remote.api

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import ru.skypaws.mobileapp.data.datasource.local.user.UserLocalDataSource

class TokenInterceptor(
    private val userLocalDataSource: UserLocalDataSource,
    private val isAviabit: Boolean = false
) {
    suspend fun intercept(
        request: HttpRequestBuilder,
    ): HttpRequestBuilder {
        if (isAviabit){
            val apiKey = userLocalDataSource.getApiKey()
            if (apiKey != null) {
                request.header("Cookie", apiKey)
            }
        } else {
            val token = userLocalDataSource.getAccessToken()
            if (token != null) {
                request.header("X-API-Key", token)
            }
        }

        return request
    }
}