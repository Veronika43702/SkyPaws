package ru.skypaws.mobileapp.data.di.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.skypaws.data.BuildConfig
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiAviabitService
import ru.skypaws.mobileapp.data.datasource.remote.api.LoggingInterceptor
import ru.skypaws.mobileapp.data.datasource.local.user.UserLocalDataSource
import ru.skypaws.mobileapp.data.datasource.remote.api.TokenInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AviabitOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AviabitApiService

@Module(includes = [JsonModule::class])
@InstallIn(SingletonComponent::class)
class ApiAviabitModule {

    companion object {
        private const val URL_AVIABIT = BuildConfig.AVIABIT_URL
    }

    @Provides
    @Singleton
    @AviabitOkHttpClient
    fun provideOkHttpClient(
        userLocalDataSource: UserLocalDataSource,
        json: Json
    ): HttpClient {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                logger = LoggingInterceptor()
                level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
            }
            install(HttpTimeout) {
                connectTimeoutMillis = 7000 // 7 seconds
                socketTimeoutMillis = 25000 // 25 seconds
            }
            defaultRequest {
                url(URL_AVIABIT)
                header(
                    "Sec-Ch-Ua",
                    "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\""
                )
                header("Sec-Ch-Ua-Mobile", "?0")
                header("Sec-Ch-Ua-Platform", "\"Windows\"")
                header("Sec-Fetch-Dest", "empty")
                header("Sec-Fetch-Mode", "cors")
                header("Sec-Fetch-Site", "same-origin")
                header(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"
                )
            }
        }

        client.plugin(HttpSend).intercept { request ->
            val tokenInterceptor = TokenInterceptor(userLocalDataSource, true)
            val newRequest = tokenInterceptor.intercept(request)
            execute(newRequest)
        }

        return client
    }

    @Provides
    @Singleton
    @AviabitApiService
    fun provideAviabitApiService(@AviabitOkHttpClient client: HttpClient): ApiAviabitService {
        return ApiAviabitService(client)
    }
}