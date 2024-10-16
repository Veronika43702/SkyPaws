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
import ru.skypaws.mobileapp.data.datasource.local.user.UserLocalDataSource
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.datasource.remote.api.LoggingInterceptor
import ru.skypaws.mobileapp.data.datasource.remote.api.TokenInterceptor
import ru.skypaws.mobileapp.data.utils.version
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainApiService

@Module(includes = [JsonModule::class])
@InstallIn(SingletonComponent::class)
class ApiModule {

    companion object {
        private const val URL_BASE_API = BuildConfig.BASE_URL_API
    }

    @Provides
    @Singleton
    @MainOkHttpClient
    fun provideHttpClient(
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
                url(URL_BASE_API)
                header("User-Agent", "SkyApp/${version.substring(1)}")
            }
        }

        client.plugin(HttpSend).intercept { request ->
            val tokenInterceptor = TokenInterceptor(userLocalDataSource)
            val newRequest = tokenInterceptor.intercept(request)
            execute(newRequest)
        }

        return client
    }

    @Provides
    @Singleton
    @MainApiService
    fun provideApiService(@MainOkHttpClient client: HttpClient): ApiService {
        return ApiService(client)
    }
}