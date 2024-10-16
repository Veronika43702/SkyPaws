package ru.skypaws.mobileapp.data.repository.logbook

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.skypaws.mobileapp.data.datasource.remote.api.ApiService
import ru.skypaws.mobileapp.data.datasource.db.dao.LogbookDao
import ru.skypaws.mobileapp.data.datasource.remote.LogbookRemoteDataSource
import ru.skypaws.mobileapp.data.di.api.MainApiService
import ru.skypaws.mobileapp.data.di.utils.DispatcherIO
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.data.error.LogErrors
import ru.skypaws.mobileapp.data.model.dto.LogbookDto
import ru.skypaws.mobileapp.data.model.entity.toEntity
import ru.skypaws.mobileapp.domain.model.FlightTime
import ru.skypaws.mobileapp.domain.model.MonthType
import ru.skypaws.mobileapp.domain.model.YearMonth
import ru.skypaws.mobileapp.domain.model.YearMonthType
import ru.skypaws.mobileapp.domain.model.YearType
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookRepository
import java.io.IOException
import javax.inject.Inject

class LogbookRepositoryImpl @Inject constructor(
    @MainApiService private val apiService: ApiService,
    private val logbookDao: LogbookDao,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val logbookRemoteDataSource: LogbookRemoteDataSource,
) : LogbookRepository {
    /**
     * Fetches logbook data [List(LogbookDto)][LogbookDto]
     * from remote source.
     *
     * Updates list to handle duplicated month by
     * [changeTypeForCurrentMonth(List(LogbookDto))][changeTypeForCurrentMonth].
     *
     * Saves data in local database [LogbookDao.insertLogbook()][LogbookDao.insertLogbook].
     *
     * Catches
     * - ApiError:
     * if [e.code][ApiErrors] = -1 (any exceptions during api request except ApiErrors),
     * then throws Exception.
     * In other case (unsuccessful response code) throws ApiErrors
     * - IOException
     * - Exception
     *
     * @throws ApiErrors - when response from server is unsuccessful (code not within 200..299)
     * @throws IOException
     * @throws Exception
     */
    override suspend fun fetchLogbookFromServerAndSaveIt() {
        withContext(dispatcherIO) {
            try {
                val body = logbookRemoteDataSource.getLogbook()

                // set item.type = 2 for current month (if there is month twice with type 0 and 3)
                val flownFlights = changeTypeForCurrentMonth(body)

                logbookDao.insertLogbook(flownFlights.toEntity())

            } catch (e: ApiErrors) {
                if (e.code != -1) {
                    throw e
                } else {
                    throw Exception()
                }
            } catch (e: IOException) {
                apiService.sendError(
                    LogErrors.fromException("LogbookRepositoryImpl: getLogbook: IOException", e)
                )
                throw e
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException("LogbookRepositoryImpl: getLogbook: Exception", e)
                )
                throw e
            }
        }
    }

    /**
     * Changes type for duplicated month.
     *
     * First duplicated item has type 0 (past events), second - type 3 (future events).
     *
     * For first item type is set as 2 and duplicated month with type 3 is deleted.
     * There can be other single future months with type 3.
     *
     *  @return [List(LogbookDto)][LogbookDto]
     */
    private fun changeTypeForCurrentMonth(body: List<LogbookDto>) = body.mapIndexed { index, item ->
        if (index != body.lastIndex &&
            item.month == body[index + 1].month &&
            item.year == body[index + 1].year
        ) {
            item.copy(type = 2)
        } else item
        // delete item with future flights (double month with type 3)
    }.filterIndexed { index, item ->
        index == 0 || !(item.month == body[index - 1].month && item.year == body[index - 1].year)
    }


    /**
     * Gets [year list][YearType] from local database: [LogbookDao.getYearList()][LogbookDao.getYearList]
     * @return List<[YearMonthType]> or emptyList()
     */
    override suspend fun getYearList(): List<YearType> =
        withContext(dispatcherIO) {
            try {
                logbookDao.getYearList()
            } catch (e: Exception) {
                emptyList()
            }
        }


    /**
     * Gets [YearMonthType][YearMonthType] in order from local database:
     * [LogbookDao.getYearMonthTypeOrderedList()][LogbookDao.getYearMonthTypeOrderedList]
     * @return List<[YearMonthType]> or emptyList()
     */
    override suspend fun getYearMonthTypeOrderedList(): List<YearMonthType> =
        withContext(dispatcherIO) {
            try {
                logbookDao.getYearMonthTypeOrderedList()
            } catch (e: Exception) {
                emptyList()
            }
        }

    /**
     * Gets [YearMonth]in order from local database:
     * [LogbookDao.getYearMonthList()][LogbookDao.getYearMonthList]
     * @return List<[YearMonth]> or emptyList()
     */
    override suspend fun getYearMonthList(): List<YearMonth> =
        withContext(dispatcherIO) {
            try {
                logbookDao.getYearMonthList()
            } catch (e: Exception) {
                emptyList()
            }
        }

    /**
     * Gets [MonthType] list for [year]
     * from local database: [LogbookDao.getMonthListByCurrentYear()][LogbookDao.getMonthListByCurrentYear]
     * @param year for which month list is taken
     * @return List<[MonthType]> or emptyList()
     */
    override suspend fun getMonthTypeListByCurrentYear(year: Int): List<MonthType> =
        withContext(dispatcherIO) {
            try {
                logbookDao.getMonthListByCurrentYear(year)
            } catch (e: Exception) {
                emptyList()
            }
        }


    /**
     * Gets [YearMonth] list from local database:
     * [LogbookDao.getYearsAndMonthsFromPrevious(year, month)][LogbookDao.getYearsAndMonthsFromPrevious]
     * @param yearOfPreviousMonth [Int] - year of max - 1 month from local database,
     * @param previousMonth [Int] - max -1  month from local database,
     * @return  List<[YearMonth]>
     *     for period between params (year, month) until latest
     */
    override suspend fun getYearAndMonthListFromPrevious(
        yearOfPreviousMonth: Int,
        previousMonth: Int
    ): List<YearMonth> =
        withContext(dispatcherIO) {
            logbookDao.getYearsAndMonthsFromPrevious(yearOfPreviousMonth, previousMonth)
        }


    /**
     * Gets summary [FlightTime] for all logbook items.
     * by [LogbookDao.sumTimeFlight()][LogbookDao.sumTimeFlight]
     * @return [FlightTime]
     */
    override suspend fun getTotalTime(): FlightTime =
        withContext(dispatcherIO) {
            logbookDao.sumTimeFlight()
        }

    /**
     * Gets summary [FlightTime] for specific [year] and [month]
     * from local database by [LogbookDao.sumTimeFlightByMonth()][LogbookDao.sumTimeFlightByMonth]
     * @param [year] to which month belongs
     * @param [month] for which time is calculated
     * @return [FlightTime]
     */
    override suspend fun getTotalTimeByMonth(year: Int, month: Int): FlightTime =
        withContext(dispatcherIO) {
            logbookDao.sumTimeFlightByMonth(year, month)
        }

    /**
     * Gets summary [FlightTime] for specific [year]
     * from local database by [LogbookDao.sumTimeFlightByYear()][LogbookDao.sumTimeFlightByYear]
     * @param [year] for which time is calculated
     * @return [FlightTime]
     */
    override suspend fun getTotalTimeByYear(year: Int): FlightTime =
        withContext(dispatcherIO) {
            try {
                logbookDao.sumTimeFlightByYear(year)
            } catch (e: Exception) {
                FlightTime(0, 0, 0)
            }
        }

    /**
     * Gets summary [FlightTime]
     * for all years until current [year] including from local database
     * by [LogbookDao.sumTimeFlightWithYear()][LogbookDao.sumTimeFlightWithYear]
     * @return [FlightTime]
     */
    override suspend fun getTotalTimeWithYear(year: Int): FlightTime =
        withContext(dispatcherIO) {
            try {
                logbookDao.sumTimeFlightWithYear(year)
            } catch (e: Exception) {
                FlightTime(0, 0, 0)
            }
        }

    /**
     * Deletes all logbook data in [LogbookDao][LogbookDao.deleteLogbook]
     */
    override suspend fun deleteLogbook() {
        withContext(dispatcherIO) {
            logbookDao.deleteLogbook()
        }
    }
}