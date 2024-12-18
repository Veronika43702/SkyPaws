package ru.skypaws.mobileapp.data.repository.logbook

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.skypaws.mobileapp.data.datasource.db.dao.LogbookDao
import ru.skypaws.mobileapp.data.datasource.db.dao.LogbookFlightDao
import ru.skypaws.mobileapp.data.di.utils.DispatcherIO
import ru.skypaws.mobileapp.data.di.utils.DispatchersDefault
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.domain.model.FlightTime
import ru.skypaws.mobileapp.domain.model.LogbookFlight
import ru.skypaws.mobileapp.domain.model.LogbookModelData
import ru.skypaws.mobileapp.domain.model.MonthType
import ru.skypaws.mobileapp.domain.model.YearMonth
import ru.skypaws.mobileapp.domain.model.YearMonthType
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookAndFlightRepository
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookFlightRepository
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookRepository
import java.time.ZoneOffset.UTC
import java.util.Locale
import javax.inject.Inject

class LogbookAndFlightRepositoryImpl @Inject constructor(
    private val logbookRepository: LogbookRepository,
    private val flightRepository: LogbookFlightRepository,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    @DispatchersDefault private val dispatcherDefault: CoroutineDispatcher,
) : LogbookAndFlightRepository {

    /**
     * Fetches logbook and flight data from server and saves it.
     *
     * Calls two functions:
     * - [fetchLogbookFromServer()][LogbookRepositoryImpl.fetchLogbookFromServerAndSaveIt]
     * for logbook data
     * - [fetchLogbookFlightsFromServer(year, month)][LogbookFlightRepositoryImpl.fetchLogbookFlightsFromServerAndSaveIt]
     * for each pair of [YearMonth] in database. The list of pairs is generated by
     * [getYearMonthList()][LogbookRepositoryImpl.getYearMonthList]
     * function.

     * @throws ApiErrors when response from server is unsuccessful (code not within 200..299)
     * @throws Exception in other cases
     */
    override suspend fun fetchLogbookAndFlightsFromServer() {
        withContext(dispatcherIO) {
            try {
                logbookRepository.fetchLogbookFromServerAndSaveIt()

                val logbookItems = logbookRepository.getYearMonthList()
                logbookItems.forEach {
                    flightRepository.fetchLogbookFlightsFromServerAndSaveIt(it.year, it.month)
                }
            } catch (e: ApiErrors) {
                throw e
            } catch (e: Exception) {
                throw e
            }
        }
    }

    /**
     * Fetches logbook data from server and fetches logbook flight data for months in period
     * from previous of max in local database until the latest from server
     * or for all in case there's no items or only one item of logbook flight data in database.
     *
     * Firstly gets latest month and its year from local database.
     *
     * In case latestMonth and latestYear != 0 calls three functions:
     * - [fetchLogbookFromServer()][LogbookRepositoryImpl.fetchLogbookFromServerAndSaveIt]
     * to update logbook data
     * - [getYearAndMonthListFromPrevious(year, month)][LogbookRepositoryImpl.getYearAndMonthListFromPrevious]
     * to get list of [YearMonth] starting from previous month until latest from local database
     * (which is updated by previous function).
     * - [fetchLogbookFlightsFromServer(year, month)][LogbookFlightRepositoryImpl.fetchLogbookFlightsFromServerAndSaveIt]
     * for each pair of [YearMonth] to get logbook flight data.
     *
     * Each month is a new request, so this logic is needed to minimize quantity of requests
     * if database is not empty and item count is more than 1.
     *
     * In case there's no item in database (latestMonth and latestYear = 0),
     * calls function:
     * - [fetchLogbookAndAllFlightsFromServer()][fetchLogbookAndFlightsFromServer]
     * to get logbook and all logbook flight data.
     *
     * @throws ApiErrors when response from server is unsuccessful (code not within 200..299)
     * @throws Exception in other cases
     */
    override suspend fun fetchAllOrUpdateLogbookAndFlightsFromServer() {
        withContext(dispatcherDefault) {
            try {
                val (latestYear, latestMonth) = flightRepository.getLatestYearMonth()

                if (latestYear != 0 && latestMonth != 0) {
                    val previousMonth = if (latestMonth == 1) {
                        12
                    } else {
                        latestMonth - 1
                    }

                    val yearOfPreviousMonth = if (latestMonth == 1) {
                        latestYear - 1
                    } else {
                        latestYear
                    }

                    logbookRepository.fetchLogbookFromServerAndSaveIt()

                    val logbookItems = logbookRepository.getYearAndMonthListFromPrevious(
                        yearOfPreviousMonth,
                        previousMonth
                    )
                    logbookItems.forEach {
                        flightRepository.fetchLogbookFlightsFromServerAndSaveIt(it.year, it.month)
                    }
                } else {
                    fetchLogbookAndFlightsFromServer()
                }
            } catch (e: ApiErrors) {
                throw e
            } catch (e: Exception) {
                throw e
            }
        }
    }

    /**
     * Gets all logbook and flight data from local storage.
     *
     * Calls functions async:
     * - monthListDeferred = [LogbookRepository.getYearMonthOrderedList()]
     * [LogbookRepository.getYearMonthTypeOrderedList]
     * - flightsDeferred= [LogbookRepository.getAllFlightsList()]
     * [LogbookFlightRepository.getAllFlightsList].
     * - logbookYearsDeferred = [LogbookRepository.getYearList()]
     * [LogbookRepository.getYearList]
     * - totalFlightDeferred = [LogbookRepository.getTotalFlight()]
     * [LogbookRepository.getTotalTime]
     *
     * Calls [getTimeAndCalculateByMonth], [calculateTimeForFlights] and [calculateTotalTime]
     * to calculate times and update [LogbookModelData].
     *
     * @return [LogbookModelData]
     */
    override suspend fun getAllLogbookData(): LogbookModelData {
        return withContext(dispatcherDefault) {
            val monthListDeferred = async { logbookRepository.getYearMonthTypeOrderedList() }
            val flightsDeferred = async { flightRepository.getAllFlightsList() }
            val logbookYearsDeferred = async { logbookRepository.getYearList() }
            val totalFlightDeferred = async { logbookRepository.getTotalTime() }

            // Загрузка данных и кэширование по месяцам
            val timeByMonth = getTimeAndCalculateByMonth(monthListDeferred.await())

            // Загрузка данных и кэширование по полетам
            val timeForFlights = calculateTimeForFlights(flightsDeferred.await())

            val totalTime = calculateTotalTime(totalFlightDeferred.await())

            LogbookModelData(
                totalFlight = totalTime.totalFlight,
                totalBlock = totalTime.totalBlock,
                totalNight = totalTime.totalNight,
                listOfYear = logbookYearsDeferred.await(),
                flightFlight = timeForFlights.flightFlight,
                flightBlock = timeForFlights.flightBlock,
                flightNight = timeForFlights.flightNight,
                totalFlightByMonth = timeByMonth.totalFlightByMonth,
                totalBlockByMonth = timeByMonth.totalBlockByMonth,
                totalNightByMonth = timeByMonth.totalNightByMonth
            )
        }
    }

    /**
     * Gets and calculate [FlightTime] for each month in [monthList].
     *
     * Adds new pairs to
     * - **updatedFlightByMonth** <- pair<[YearMonth][YearMonthType], [flight time][Int]>
     * - **updatedBlockByMonth** <- pair<[YearMonth][YearMonthType], [block time][Int]>
     * - **updatedNightByMonth** <- pair<[YearMonth][YearMonthType], [night time][Int]>
     *
     * and returns [LogbookModelData] with updated **total*Time*ByMonth**.
     *
     * @return [LogbookModelData]
     * @see [getHoursAndMinutes]
     */
    override suspend fun getTimeAndCalculateByMonth(monthList: List<YearMonthType>): LogbookModelData {
        val updatedFlightByMonth = mutableMapOf<YearMonthType, String>()
        val updatedBlockByMonth = mutableMapOf<YearMonthType, String>()
        val updatedNightByMonth = mutableMapOf<YearMonthType, String>()

        monthList.forEach { yearMonthType ->
            val flightTime = logbookRepository.getTotalTimeByMonth(
                yearMonthType.year,
                yearMonthType.month
            )
            updatedFlightByMonth[yearMonthType] = getHoursAndMinutes(flightTime.flight)
            updatedBlockByMonth[yearMonthType] = getHoursAndMinutes(flightTime.block)
            updatedNightByMonth[yearMonthType] = getHoursAndMinutes(flightTime.night)
        }

        return LogbookModelData(
            totalFlightByMonth = updatedFlightByMonth,
            totalBlockByMonth = updatedBlockByMonth,
            totalNightByMonth = updatedNightByMonth
        )
    }

    /**
     * Calculates [FlightTime] for each flight in [flights] and adds pairs to
     * - **flightTimes** <- pair<[LogbookFlight], [flight time][Int]>
     * - **flightBlock** <- pair<[LogbookFlight], [block time][Int]>
     * - **flightNight** <- pair<[LogbookFlight], [night time][Int]>
     *
     * returning [LogbookModelData] with updated **flight*Time***
     *
     * @return [LogbookModelData]
     * @see [getHoursAndMinutes]
     */
    override fun calculateTimeForFlights(flights: List<LogbookFlight>): LogbookModelData {
        val flightTimes =
            flights.associateWith { flight -> getHoursAndMinutes(flight.timeFlight) }
        val flightBlocks =
            flights.associateWith { flight -> getHoursAndMinutes(flight.timeBlock) }
        val flightNights =
            flights.associateWith { flight -> getHoursAndMinutes(flight.timeNight) }

        return LogbookModelData(
            flightFlight = flightTimes,
            flightBlock = flightBlocks,
            flightNight = flightNights,
        )
    }


    /**
     * Calculates total [FlightTime] and adds pairs to
     * - **totalBlock** <- [totalBlockFlightNight.flight][Int]>
     * - **totalFlight** <- [totalBlockFlightNight.block][Int]>
     * - **totalNight** <- [totalBlockFlightNight.night][Int]>
     *
     * returning [LogbookModelData] with updated **total*Time***
     * @return [LogbookModelData]
     * @see [getHoursAndMinutes]
     */
    override fun calculateTotalTime(totalTime: FlightTime): LogbookModelData = LogbookModelData(
        totalFlight = getHoursAndMinutes(totalTime.flight),
        totalBlock = getHoursAndMinutes(totalTime.block),
        totalNight = getHoursAndMinutes(totalTime.night),
    )


    /**
     * Gets [MonthType] list for [year] from local database.
     * @param year for which month list is taken
     * @return List<[MonthType]>
     */
    override suspend fun getMonthTypeListByCurrentYear(year: Int): List<MonthType> =
        logbookRepository.getMonthTypeListByCurrentYear(year)

    /**
     * Gets [LogbookFlight] list for [yearMonth] from local database.
     * @param yearMonth for which flight are taken.
     * @return List<[LogbookFlight]>
     */
    override suspend fun getFlightListByYearMonth(yearMonth: YearMonth): List<LogbookFlight> =
        flightRepository.getFlightListByYearMonth(yearMonth)

    /**
     * Gets summary [FlightTime] for specific [year]
     * from local database by [LogbookDao.sumTimeFlightByYear()][LogbookDao.sumTimeFlightByYear]
     * @param [year] for which time is calculated
     * @return [FlightTime]
     */
    override suspend fun getTotalTimeByYear(year: Int): FlightTime =
        logbookRepository.getTotalTimeByYear(year)


    /**
     * Gets summary [FlightTime]
     * for all years until current [year] including from local database
     * by [LogbookDao.sumTimeFlightWithYear()][LogbookDao.sumTimeFlightWithYear]
     * @return [FlightTime]
     */
    override suspend fun getTotalTimeWithYear(year: Int): FlightTime =
        logbookRepository.getTotalTimeWithYear(year)


    /**
     * Deletes all items in [LogbookDao][LogbookDao.deleteLogbook] and
     * [LogbookFlightDao][LogbookFlightDao.deleteAllLogbookFlights]
     */
    override suspend fun deleteLogbookAndFlights() {
        try {
            logbookRepository.deleteLogbook()
            flightRepository.deleteLogbookFlights()
        } catch (_: Exception) {
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