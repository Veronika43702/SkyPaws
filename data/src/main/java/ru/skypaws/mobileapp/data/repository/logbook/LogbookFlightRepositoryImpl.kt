package ru.skypaws.mobileapp.data.repository.logbook

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.datasource.db.dao.LogbookFlightDao
import ru.skypaws.mobileapp.data.datasource.remote.LogbookFlightRemoteDataSource
import ru.skypaws.mobileapp.data.di.api.MainApiService
import ru.skypaws.mobileapp.data.di.utils.DispatcherIO
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.data.error.LogErrors
import ru.skypaws.mobileapp.data.mapper.logbookFlightDomainToEntity
import ru.skypaws.mobileapp.data.mapper.logbookFlightEntityToDomain
import ru.skypaws.mobileapp.data.mapper.toDomain
import ru.skypaws.mobileapp.domain.model.LogbookFlight
import ru.skypaws.mobileapp.domain.model.YearMonth
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookFlightRepository
import java.io.IOException
import javax.inject.Inject

class LogbookFlightRepositoryImpl @Inject constructor(
    @MainApiService private val apiService: ApiService,
    private val logbookFlightDao: LogbookFlightDao,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val logbookFlightRemoteDataSource: LogbookFlightRemoteDataSource
) : LogbookFlightRepository {

    /**
     * Fetches [LogbookFlight][ru.skypaws.mobileapp.model.LogbookFlight] from remoteDataSource
     * for specific [year] and [month] and saves it locally.
     *
     * Before saving new data
     * [LogbookFlightDao.insertLogbookFlights()][LogbookFlightDao.insertLogbookFlights]
     * deletes old flight data for [year] and [month].
     *
     * @throws ApiErrors when request is unsuccessful
     * @throws NullPointerException
     * @throws IOException
     * @throws Exception in other cases
     */
    override suspend fun fetchLogbookFlightsFromServerAndSaveIt(year: Int, month: Int) {
        withContext(dispatcherIO) {
            try {
                val flights = logbookFlightRemoteDataSource.getLogbookFlights(year, month)

                // delete old items as it can be flown and primaryKey (dateflight) is different
                deleteLogbookFlightByYearMonth(year, month)

                // save new data
                saveLogbookFlight(flights)
            } catch (e: ApiErrors) {
                if (e.code != -1) {
                    throw e
                } else {
                    throw Exception()
                }
            } catch (e: NullPointerException) {
                apiService.sendError(
                    LogErrors.fromException(
                        "LogbookAndFlightRepositoryImpl: fetchLogbookFlightFromServer: NullPointerException",
                        e
                    )
                )
                throw e
            } catch (e: IOException) {
                apiService.sendError(
                    LogErrors.fromException(
                        "LogbookAndFlightRepositoryImpl: fetchLogbookFlightFromServer: IOException",
                        e
                    )
                )
                throw e
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException(
                        "LogbookAndFlightRepositoryImpl: fetchLogbookFlightFromServer: Exception",
                        e
                    )
                )
                throw e
            }
        }
    }

    /**
     * Gets all flights from local database: [LogbookFlightDao.getAllFlights()][LogbookFlightDao.getAllFlights]
     * @return List<[LogbookFlight][ru.skypaws.mobileapp.model.LogbookFlight]> or emptyList()
     */
    override suspend fun getAllFlightsList(): List<LogbookFlight> =
        withContext(dispatcherIO) {
            try {
                logbookFlightDao.getAllFlights().logbookFlightEntityToDomain()
            } catch (e: Exception) {
                emptyList()
            }
        }

    /**
     * Gets [LogbookFlight][ru.skypaws.mobileapp.model.LogbookFlight] list for specific [yearMonth]
     * from local database by [LogbookFlightDao.getFlightList()][LogbookFlightDao.getFlightList]
     * @return [LogbookFlight][ru.skypaws.mobileapp.model.LogbookFlight] or emptyList()
     */
    override suspend fun getFlightListByYearMonth(yearMonth: YearMonth): List<LogbookFlight> =
        withContext(dispatcherIO) {
            try {
                logbookFlightDao.getFlightList(yearMonth.year, yearMonth.month)
                    .map { it.toDomain() }
            } catch (e: Exception) {
                emptyList()
            }
        }


    /**
     * Gets latest year and month [YearMonth][ru.skypaws.mobileapp.model.YearMonth]
     * from local database: [LogbookDao.getMaxYearMonth()][LogbookFlightDao.getMaxYearMonth]
     * @return [YearMonth][ru.skypaws.mobileapp.model.YearMonth] - YearMonth(0,0)
     * if item count in database is less than 2
     */
    override suspend fun getLatestYearMonth(): YearMonth =
        withContext(dispatcherIO) {
            logbookFlightDao.getMaxYearMonth()
        }

    /**
     * Saves [LogbookFlight] locally
     */
    override suspend fun saveLogbookFlight(flights: List<LogbookFlight>) {
        logbookFlightDao.insertLogbookFlights(flights.logbookFlightDomainToEntity())
    }

    /**
     * Deletes [LogbookFlight] data corresponding to [year] and [month] from local storage
     * @param year year for which data must be deleted
     * @param month month for which data must be deleted
     */
    override suspend fun deleteLogbookFlightByYearMonth(year: Int, month: Int) {
        logbookFlightDao.deleteLogbookItem(year, month)
    }

    /**
     * Deletes all logbook flight data from local storage
     */
    override suspend fun deleteLogbookFlights() {
        withContext(dispatcherIO) {
            logbookFlightDao.deleteAllLogbookFlights()
        }
    }
}