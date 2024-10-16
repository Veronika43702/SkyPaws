package ru.skypaws.mobileapp.data.datasource.remote

import ru.skypaws.mobileapp.data.datasource.remote.api.ApiAviabitService
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.di.api.AviabitApiService
import ru.skypaws.mobileapp.data.di.api.MainApiService
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.data.error.LogErrors
import ru.skypaws.mobileapp.data.mapper.toDomain
import ru.skypaws.mobileapp.domain.model.LogbookFlight
import java.io.IOException
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class LogbookFlightRemoteDataSource @Inject constructor(
    @MainApiService private val apiService: ApiService,
    @AviabitApiService private val apiAviabitService: ApiAviabitService,
) {
    /**
     * Gets event data [LogbookFlight][ru.skypaws.mobileapp.model.LogbookFlight]
     * for specific [year] and [month], leaving only working flights
     * (no reserves, no flight as a passenger) by sending api request
     * [ApiAviabitService.getLogbookDetailed(eng, year, month)][ApiAviabitService.fetchLogbookFlights].
     *
     * Uses Mapper [LogbookFlightDto.toDomain([year],[month])][ru.skypaws.mobileapp.mapper.toDomain] to
     * convert type from Dto.
     *
     * @return list<[LogbookFlight][ru.skypaws.mobileapp.model.LogbookFlight]>
     * @throws ApiErrors with code -1, when any Exception happens except unsuccessful response
     * @throws ApiErrors when request is unsuccessful
     */
    suspend fun getLogbookFlights(year: Int, month: Int): List<LogbookFlight> {
        return try {
            apiAviabitService.fetchLogbookFlights(
                false,
                year = year,
                month = month
            ).filter { event ->
                event.flight.startsWith("DP") && event.chair != null
            }.map { it.toDomain(year, month) }
        } catch (e: SocketException) {
            throw ApiErrors(-1, null)
        } catch (e: UnresolvedAddressException) {
            throw ApiErrors(-1, null)
        } catch (e: ApiErrors) {
            throw e
        } catch (e: NullPointerException) {
            apiService.sendError(
                LogErrors.fromException(
                    "LogbookFlightRemoteDataSource: getLogbookItemDetailed: NullPointerException",
                    e
                )
            )
            throw ApiErrors(-1, null)
        } catch (e: IOException) {
            apiService.sendError(
                LogErrors.fromException(
                    "LogbookFlightRemoteDataSource: getLogbookItemDetailed: IOException",
                    e
                )
            )
            throw ApiErrors(-1, null)
        } catch (e: Exception) {
            apiService.sendError(
                LogErrors.fromException(
                    "LogbookFlightRemoteDataSource: getLogbookItemDetailed: Exception",
                    e
                )
            )
            throw ApiErrors(-1, null)
        }
    }
}