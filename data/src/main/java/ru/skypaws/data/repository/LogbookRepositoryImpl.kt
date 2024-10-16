package ru.skypaws.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.skypaws.data.api.ApiAviabitService
import ru.skypaws.data.api.ApiService
import ru.skypaws.data.db.LogbookDao
import ru.skypaws.data.db.LogbookFlightDao
import ru.skypaws.data.di.api.AviabitApiService
import ru.skypaws.data.di.api.MainApiService
import ru.skypaws.data.error.ApiErrors
import ru.skypaws.data.error.LogErrors
import ru.skypaws.data.mapper.toDomain
import ru.skypaws.data.source.dto.LogbookDto
import ru.skypaws.data.source.entity.LogbookEntity
import ru.skypaws.data.source.entity.logbookFlightDtoToEntity
import ru.skypaws.domain.model.LogbookFlight
import ru.skypaws.domain.model.LogbookModelData
import ru.skypaws.domain.model.YearMonth
import ru.skypaws.domain.model.YearMonthType
import ru.skypaws.domain.model.YearType
import ru.skypaws.domain.repository.LogbookRepository
import java.io.IOException
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException
import java.time.ZoneOffset.UTC
import java.util.Locale
import javax.inject.Inject

class LogbookRepositoryImpl @Inject constructor(
    @MainApiService private val apiService: ApiService,
    @AviabitApiService private val apiAviabitService: ApiAviabitService,
    private val logbookDao: LogbookDao,
    private val logbookFlightDao: LogbookFlightDao,
) : LogbookRepository {

    /**
     * Calls functions async to get all logbook data and returns [LogbookModelData][LogbookModelData]:
     * - logbookYearsDeferred = [LogbookRepositoryImpl.getYearList()]
     * [ru.skypaws.data.repository.LogbookRepositoryImpl.getYearList]
     * - totalFlightDeferred = [LogbookRepositoryImpl.getTotalFlight()]
     * [ru.skypaws.data.repository.LogbookRepositoryImpl.getTotalFlight]
     * - monthListDeferred = [LogbookRepositoryImpl.getAllMonth()]
     * [ru.skypaws.data.repository.LogbookRepositoryImpl.getAllMonth]
     * - flightsDeferred= [LogbookRepositoryImpl.getAllFlights()]
     * [ru.skypaws.data.repository.LogbookRepositoryImpl.getAllFlights].
     *
     * As [monthListDeferred][ru.skypaws.domain.model.YearMonthType] is ready adds new pairs to
     * - **updatedFlightByMonth** <- pair<[YearMonthType][ru.skypaws.domain.model.YearMonthType], [flight time][Int]>
     * - **updatedBlockByMonth** <- pair<[YearMonthType][ru.skypaws.domain.model.YearMonthType], [block time][Int]>
     * - **updatedNightByMonth** <- pair<[YearMonthType][ru.skypaws.domain.model.YearMonthType], [night time][Int]>.
     *
     * As [flightsDeferred][ru.skypaws.domain.model.LogbookFlight] is ready adds new pairs to
     * - **flightTimes** <- pair<[LogbookFlight][ru.skypaws.domain.model.LogbookFlight], [flight time][Int]>
     * - **flightBlock** <- pair<[LogbookFlight][ru.skypaws.domain.model.LogbookFlight], [block time][Int]>
     * - **flightNight** <- pair<[LogbookFlight][ru.skypaws.domain.model.LogbookFlight], [night time][Int]>.
     *
     * As [logbookYearsDeferred][ru.skypaws.domain.model.YearType] is ready adds value to **logbookYears**.
     *
     * As [totalFlightDeferred][ru.skypaws.domain.model.FlightTime] is ready calculates:
     * - **totalBlock** <- [totalBlockFlightNight.flight][Int]>
     * - **totalFlight** <- [totalBlockFlightNight.block][Int]>
     * - **totalNight** <- [totalBlockFlightNight.night][Int]>
     *
     * @return [LogbookModelData][LogbookModelData]
     * @see [getHoursAndMinutes]
     */
    override suspend fun getAllLogbookData(): LogbookModelData {
        return withContext(Dispatchers.IO) {
            val monthListDeferred = async { getAllMonth() }
            val flightsDeferred = async { getAllFlights() }
            val logbookYearsDeferred = async { getYearList() }
            val totalFlightDeferred = async { getTotalFlight() }


            // Загрузка и кэширование данных по месяцам
            val monthList = monthListDeferred.await()
            val updatedFlightByMonth = mutableMapOf<YearMonthType, String>()
            val updatedBlockByMonth = mutableMapOf<YearMonthType, String>()
            val updatedNightByMonth = mutableMapOf<YearMonthType, String>()

            monthList.forEach { yearMonthType ->
                val flightTime = getTotalFlightByMonth(
                    yearMonthType.year,
                    yearMonthType.month
                )
                updatedFlightByMonth[yearMonthType] = getHoursAndMinutes(flightTime.flight)
                updatedBlockByMonth[yearMonthType] = getHoursAndMinutes(flightTime.block)
                updatedNightByMonth[yearMonthType] = getHoursAndMinutes(flightTime.night)
            }

            // Загрузка данных и кэширование по полетам
            val flights = flightsDeferred.await()
            val flightTimes =
                flights.associateWith { flight -> getHoursAndMinutes(flight.timeFlight) }
            val flightBlocks =
                flights.associateWith { flight -> getHoursAndMinutes(flight.timeBlock) }
            val flightNights =
                flights.associateWith { flight -> getHoursAndMinutes(flight.timeNight) }

            val logbookYears = logbookYearsDeferred.await()

            val totalBlockFlightNight = totalFlightDeferred.await()
            val totalBlock = getHoursAndMinutes(totalBlockFlightNight.block)
            val totalFlight = getHoursAndMinutes(totalBlockFlightNight.flight)
            val totalNight = getHoursAndMinutes(totalBlockFlightNight.night)

            LogbookModelData(
                totalBlock = totalBlock,
                totalFlight = totalFlight,
                totalNight = totalNight,
                listOfYear = logbookYears,
                flightFlight = flightTimes,
                flightBlock = flightBlocks,
                flightNight = flightNights,
                totalFlightByMonth = updatedFlightByMonth,
                totalBlockByMonth = updatedBlockByMonth,
                totalNightByMonth = updatedNightByMonth
            )
        }
    }

    /**
     * Gets year list from local database: [LogbookDao.getYearList()][LogbookDao.getYearList]
     * @return List<[ru.skypaws.domain.model.YearType]>
     */
    override suspend fun getYearList(): List<YearType> =
        withContext(Dispatchers.IO) {
            logbookDao.getYearList().map { YearType(it.year, it.type) }
        }

    /**
     * Changes type for duplicated month (first duplicated item has type 0 (past events),
     * second - type 3 (future events)).
     * For first item type is set as 2 and duplicated month with type 3 is deleted.
     * There can be other single future months with type 3.
     *
     *  @return [List(LogbookDto)][ru.skypaws.data.source.dto.LogbookDto]
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
     * Send api request [ApiAviabitService.getLogbook()][ApiAviabitService.getLogbook]
     * to get logbook data [List(LogbookDto)][ru.skypaws.data.source.dto.LogbookDto]
     * from server.
     *
     * Changes type for duplicated month
     * [changeTypeForCurrentMonth(List(LogbookDto))][changeTypeForCurrentMonth].
     * Sets item type as 2 for current month (if there is month duplicated with type 0 and 3).
     *
     * Saves data in local database [LogbookDao.insertLogbook()][LogbookDao.insertLogbook].
     *
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun getLogbook() {
        withContext(Dispatchers.IO) {
            try {
                val body = apiAviabitService.getLogbook(false)

                // set item.type = 3 for current month (if there is month twice with type 0 and 3)
                val flownFlights = changeTypeForCurrentMonth(body)

                logbookDao.insertLogbook(flownFlights.map { LogbookEntity.fromDto(it) })

            } catch (e: SocketException) {
                throw Exception()
            } catch (e: UnresolvedAddressException) {
                throw Exception()
            } catch (e: ApiErrors) {
                throw ApiErrors(e.code, e.message)
            } catch (e: IOException) {
                apiService.sendError(
                    LogErrors.fromException("LogbookRepositoryImpl: getLogbook: IOException", e)
                )
                throw Exception()
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException("LogbookRepositoryImpl: getLogbook: Exception", e)
                )
                throw Exception()
            }
        }
    }


    /**
     * Send api request [ApiAviabitService.getLogbookDetailed(eng, year, month)][ApiAviabitService.getLogbookDetailed]
     * to get event data for specific year and month, leaving only working flights (no reserves, no flight as passenger).
     *
     * Deletes old flight data for year and month.
     *
     * Saves data in local database [LogbookFlightDao.insertLogbookDetailed()][LogbookFlightDao.insertLogbookDetailed].
     *
     * @throws ApiErrors
     * @throws Exception
     */
    override suspend fun getLogbookItemDetailed(year: Int, month: Int) {
        withContext(Dispatchers.IO) {
            try {
                val flights = apiAviabitService.getLogbookDetailed(
                    false,
                    year = year,
                    month = month
                ).filter { event ->
                    event.flight.startsWith("DP") && event.chair != null
                }.map { it.copy(year = year, month = month) }

                // delete old items as it can be flown and primaryKey (dateflight) is different
                logbookFlightDao.deleteLogbookItem(year, month)
                // insert new data
                logbookFlightDao.insertLogbookDetailed(flights.logbookFlightDtoToEntity())
            } catch (e: SocketException) {
                throw Exception()
            } catch (e: UnresolvedAddressException) {
                throw Exception()
            } catch (e: ApiErrors) {
                throw ApiErrors(e.code, e.message)
            } catch (e: NullPointerException) {
                apiService.sendError(
                    LogErrors.fromException(
                        "LogbookRepositoryImpl: getLogbookItemDetailed: NullPOinterException",
                        e
                    )
                )
                throw Exception()
            } catch (e: IOException) {
                apiService.sendError(
                    LogErrors.fromException(
                        "LogbookRepositoryImpl: getLogbookItemDetailed: IOException",
                        e
                    )
                )
                throw Exception()
            } catch (e: Exception) {
                apiService.sendError(
                    LogErrors.fromException(
                        "LogbookRepositoryImpl: getLogbookItemDetailed: Exception",
                        e
                    )
                )
                throw Exception()
            }
        }
    }

    /**
     * Gets data from local database: [LogbookDao.getYearAndMonth()][LogbookDao.getYearAndMonth]
     * @return List<[ru.skypaws.domain.model.YearMonth]>
     */
    override suspend fun getLogbookDetailed(): List<YearMonth> =
        withContext(Dispatchers.IO) { logbookDao.getYearAndMonth() }

    /**
     * Gets data from local database: [LogbookDao.getYearsAndMonthsByCurrent(year, month)][LogbookDao.getYearsAndMonthsByCurrent]
     * @param year [Int] - previous year from max in local database,
     * @param month [Int] - previous month from max in local database,
     * @return List<[YearMonth][ru.skypaws.domain.model.YearMonth]> from params (year, month) until latest
     */
    override suspend fun getYearsAndMonthsByCurrent(year: Int, month: Int): List<YearMonth> =
        withContext(Dispatchers.IO) {
            logbookDao.getYearsAndMonthsByCurrent(year, month)
        }

    /**
     * Gets latest month from local database: [LogbookDao.getMaxMonthOfMaxYear()][LogbookDao.getMaxMonthOfMaxYear]
     * @return month [Int]
     */
    override suspend fun getLatestMonth(): Int =
        withContext(Dispatchers.IO) {
            logbookDao.getMaxMonthOfMaxYear()
        }

    /**
     * Gets latest year from local database: [LogbookDao.getMaxYear()][LogbookDao.getMaxYear]
     * @return year [Int]
     */
    override suspend fun getLatestYear(): Int =
        withContext(Dispatchers.IO) {
            logbookDao.getMaxYear()
        }

    /**
     * Gets month list for [year] from local database: [LogbookDao.getMonthList()][LogbookDao.getMonthList]
     * @return List<[ru.skypaws.domain.model.YearMonthType]>
     */
    override suspend fun getMonthList(year: Int): List<YearMonthType> =
        withContext(Dispatchers.IO) {
            logbookDao.getMonthList(year)
        }

    /**
     * Gets all months from local database: [LogbookDao.getAllMonth()][LogbookDao.getAllMonth]
     * @return List<[ru.skypaws.domain.model.YearMonthType]>
     */
    override suspend fun getAllMonth(): List<YearMonthType> =
        withContext(Dispatchers.IO) {
            logbookDao.getAllMonth()
        }

    /**
     * Gets all flights from local database: [LogbookFlightDao.getAllFlights()][LogbookFlightDao.getAllFlights]
     * @return List<[ru.skypaws.domain.model.LogbookFlight]>
     */
    override suspend fun getAllFlights(): List<LogbookFlight> {
        return withContext(Dispatchers.IO) {
            val flightEntities = logbookFlightDao.getAllFlights()
            flightEntities.map { it.toDomain() }
        }
    }

    /**
     * Calls [LogbookFlightDao.getFlightList()][LogbookFlightDao.getFlightList]
     * to get [LogbookFlight][ru.skypaws.domain.model.LogbookFlight] for specific [year] and [month]
     * from local database.
     * @return [ru.skypaws.domain.model.LogbookFlight]
     */
    override suspend fun getFlightListByYearMonth(year: Int, month: Int): List<LogbookFlight> {
        return withContext(Dispatchers.IO) {
            val flightListEntity = logbookFlightDao.getFlightList(year, month)
            flightListEntity.map { it.toDomain() }
        }
    }

    /**
     * Calls [LogbookDao.sumTimeFlight()][LogbookDao.sumTimeFlight]
     * to get total [FlightTime][ru.skypaws.domain.model.FlightTime]
     * for all years from local database.
     * @return [ru.skypaws.domain.model.FlightTime]
     */
    override suspend fun getTotalFlight() = withContext(Dispatchers.IO) {
        logbookDao.sumTimeFlight()
    }

    /**
     * Calls [LogbookDao.sumTimeFlightByYear()][LogbookDao.sumTimeFlightByYear]
     * to get total [FlightTime][ru.skypaws.domain.model.FlightTime] for specific [year]
     * from local database.
     * @param [year] for which time is calculated
     * @return [ru.skypaws.domain.model.FlightTime]
     */
    override suspend fun getTotalFlightByYear(year: Int) = withContext(Dispatchers.IO) {
        logbookDao.sumTimeFlightByYear(year)
    }

    /**
     * Calls [LogbookDao.sumTimeFlightByMonth()][LogbookDao.sumTimeFlightByMonth]
     * to get total [FlightTime][ru.skypaws.domain.model.FlightTime] for specific [year] and [month]
     * from local database.
     * @param [year] in which month is located
     * @param [month] for which time is calculated
     * @return [ru.skypaws.domain.model.FlightTime]
     */
    override suspend fun getTotalFlightByMonth(year: Int, month: Int) =
        withContext(Dispatchers.IO) {
            logbookDao.sumTimeFlightByMonth(year, month)
        }

    /**
     * Calls [LogbookDao.sumTimeFlightWithYear()][LogbookDao.sumTimeFlightWithYear]
     * to get [FlightTime][ru.skypaws.domain.model.FlightTime] for all years until current [year]
     * including from local database.
     * @return [ru.skypaws.domain.model.FlightTime]
     */
    override suspend fun getTotalFlightWithYear(year: Int) =
        withContext(Dispatchers.IO) {
            logbookDao.sumTimeFlightWithYear(year)
        }

    /**
     * Deletes [LogbookDao][LogbookDao.deleteLogbook] and
     * [LogbookFlightDao][LogbookFlightDao.deleteLogbookDetailed]
     */
    override suspend fun delete() {
        withContext(Dispatchers.IO) {
            logbookDao.deleteLogbook()
            logbookFlightDao.deleteLogbookDetailed()
        }
    }

    /**
     * Transforms seconds to hh:mm type.
     * @param [seconds] seconds of [Int] type
     * @return [String]
     */
    private fun getHoursAndMinutes(seconds: Int): String {
        val totalMinutes = seconds / 60
        val hours = totalMinutes / 60
        val minutes = totalMinutes - hours * 60
        return String.format(
            Locale(UTC.toString()),
            "%02d:%02d",
            hours,
            minutes
        )
    }
}