package ru.skypaws.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.skypaws.data.source.dto.CrewPlanEventDto
import ru.skypaws.data.source.dto.LogbookDto
import ru.skypaws.data.source.dto.LogbookFlightDto

class ApiAviabitService(private val client: HttpClient) {
    suspend fun getCrewPlan(
        dateBegin: Long,
        dateEnd: Long,
        eng: Boolean,
        airportCode: Int
    ): List<CrewPlanEventDto> {
        val response: HttpResponse =  client.get("api/crew-plan") {
            parameter("dateBegin", dateBegin)
            parameter("dateEnd", dateEnd)
            parameter("eng", eng)
            parameter("airportCode", airportCode)
            contentType(ContentType.Application.Json)
        }
        return ApiAviabitResponseChecker.checkAviabitResponse(response) {
            response.body<List<CrewPlanEventDto>>()
        }
    }

    suspend fun getLogbook(eng: Boolean): List<LogbookDto> {
        val response: HttpResponse = client.get("api/logbook") {
            parameter("eng", eng)
        }
        return ApiAviabitResponseChecker.checkAviabitResponse(response) {
            response.body<List<LogbookDto>>()
        }
    }

    suspend fun getLogbookDetailed(eng: Boolean, year: Int, month: Int): List<LogbookFlightDto> {
        val response: HttpResponse = client.get("api/logbook-detail") {
            parameter("eng", eng)
            parameter("year", year)
            parameter("month", month)
        }
        return ApiAviabitResponseChecker.checkAviabitResponse(response) {
            response.body<List<LogbookFlightDto>>()
        }
    }
}