package ru.skypaws.features.mainActivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.skypaws.data.utils.DateUtils
import ru.skypaws.domain.model.YearMonthType
import ru.skypaws.domain.usecase.logbook.GetAllFlightsUseCase
import ru.skypaws.domain.usecase.logbook.GetAllLogbookDataUseCase
import ru.skypaws.domain.usecase.logbook.GetAllMonthUseCase
import ru.skypaws.domain.usecase.logbook.GetFlightListByYearMonthUseCase
import ru.skypaws.domain.usecase.logbook.GetMonthListUseCase
import ru.skypaws.domain.usecase.logbook.GetYearListUseCase
import ru.skypaws.domain.usecase.userPayInfo.GetLogbookExpDateUseCase
import ru.skypaws.domain.usecase.logbook.GetTotalFlightByMonthUseCase
import ru.skypaws.domain.usecase.logbook.GetTotalFlightByYearUseCase
import ru.skypaws.domain.usecase.logbook.GetTotalFlightUseCase
import ru.skypaws.domain.usecase.logbook.GetTotalFlightWithYearUseCase
import ru.skypaws.features.mainActivity.model.LogbookModel
import java.time.Instant
import java.time.ZoneOffset.UTC
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val dateUtils: DateUtils,
    private val getAllLogbookDataUseCase: GetAllLogbookDataUseCase,

    private val getYearListUseCase: GetYearListUseCase,
    private val getAllMonthUseCase: GetAllMonthUseCase,
    private val getAllFlightsUseCase: GetAllFlightsUseCase,
    private val getTotalFlightUseCase: GetTotalFlightUseCase,
    private val getTotalFlightByMonthUseCase: GetTotalFlightByMonthUseCase,

    private val getMonthListUseCase: GetMonthListUseCase,
    private val getFlightListByYearMonthUseCase: GetFlightListByYearMonthUseCase,
    private val getTotalFlightByYearUseCase: GetTotalFlightByYearUseCase,
    private val getTotalFlightWithYearUseCase: GetTotalFlightWithYearUseCase,
    private val getLogbookExpDateUseCase: GetLogbookExpDateUseCase,
) : ViewModel() {
    private val _logbookState = MutableStateFlow(LogbookModel())
    val logbookState: StateFlow<LogbookModel> = _logbookState.asStateFlow()

    init {
        // loading of data ahead
        loadInitialData()
    }

    /**
     * Gets all logbook data and saves it in [logbookState][_logbookState]:
     * - totalBlock
     * - totalFlight
     * - totalNight
     * - listOfYear
     * - flightFlight
     * - flightBlock
     * - flightNight
     * - totalFlightByMonth
     * - totalBlockByMonth
     * - totalNightByMonth
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            val logbookDataModel = getAllLogbookDataUseCase()

            _logbookState.value = _logbookState.value.copy(
                totalBlock = logbookDataModel.totalBlock,
                totalFlight = logbookDataModel.totalFlight,
                totalNight = logbookDataModel.totalNight,
                listOfYear = logbookDataModel.listOfYear,
                flightFlight = logbookDataModel.flightFlight,
                flightBlock = logbookDataModel.flightBlock,
                flightNight = logbookDataModel.flightNight,
                totalFlightByMonth = logbookDataModel.totalFlightByMonth,
                totalBlockByMonth = logbookDataModel.totalBlockByMonth,
                totalNightByMonth = logbookDataModel.totalNightByMonth
            )
        }
    }

    /**
     * Calls function [LogbookRepositoryImpl.getMonthList()]
     * [ru.skypaws.data.repository.LogbookRepositoryImpl.getMonthList]
     * to get logbook data [YearMonthType][ru.skypaws.domain.model.YearMonthType]
     * for a specific [year] from local database.
     *
     * Updates [logbookState.monthList][_logbookState] in [LogbookViewModel]
     * with new pair<[year], List<[YearMonthType][ru.skypaws.domain.model.YearMonthType]>>
     * @param [year] year for which data is calculated.
     */
    fun getMonthList(year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val monthList = getMonthListUseCase(year)

            val updatedMonthListMap = _logbookState.value.monthList.toMutableMap()
            updatedMonthListMap[year] = monthList

            withContext(Dispatchers.Main) {
                _logbookState.value = _logbookState.value.copy(
                    monthList = updatedMonthListMap
                )
            }
        }
    }

    /**
     * Calls function [LogbookRepositoryImpl.getFlightListByYearMonth()]
     * [ru.skypaws.data.repository.LogbookRepositoryImpl.getFlightListByYearMonth]
     * to get logbook flight data [LogbookFlight][ru.skypaws.domain.model.LogbookFlight]
     * for a specific [year and month][yearMonth] from local database.
     *
     * Updates [logbookState.flightList][_logbookState] in [LogbookViewModel]
     * with new pair<[yearMonth], List<[LogbookFlight][ru.skypaws.domain.model.LogbookFlight]>>
     * @param [yearMonth] year and month for which data is calculated.
     */
    fun getFlightList(yearMonth: YearMonthType) {
        viewModelScope.launch(Dispatchers.IO) {
            val flightList = getFlightListByYearMonthUseCase(yearMonth.year, yearMonth.month)

            val updatedFlightListMap = _logbookState.value.flightList.toMutableMap()
            updatedFlightListMap[yearMonth] = flightList

            withContext(Dispatchers.Main) {
                _logbookState.value = _logbookState.value.copy(
                    flightList = updatedFlightListMap
                )
            }
        }
    }

    /**
     * Calls function [UserPayInfoRepositoryImpl.getLogbookExpDate()]
     * [ru.skypaws.data.repository.UserPayInfoRepositoryImpl.getLogbookExpDate]
     * to get logbook payment data of user from SharedPreferences.
     *
     * Check whether date is after current time.
     * @return [Boolean]: **true**, if date is in the future and **false** if in the past.
     */
    fun logbookPaidStatus(): Boolean {
        val logbookExpirationDate = getLogbookExpDateUseCase()
        val timeNow = Instant.now().epochSecond

        return (logbookExpirationDate >= timeNow)
    }

    /**
     * Calls function [LogbookRepositoryImpl.getTotalFlightByYear()]
     * [ru.skypaws.data.repository.LogbookRepositoryImpl.getTotalFlightByYear]
     * to get logbook data [FlightTime][ru.skypaws.domain.model.FlightTime]
     * for a specific [year] from local database.
     *
     * Function changes properties of [logbookState][_logbookState] in [LogbookViewModel]
     * - **totalFlightByYear** is updated by adding a new pair<year, flight time>
     * - **totalBlockByYear** is updated by adding a new pair<year, block time>
     * - **totalNightByYear** is updated by adding a new pair<year, night time>
     * @param [year] year of [Int] type for which data is calculated.
     * @see [getHoursAndMinutes]
     */
    fun getTotalByYear(year: Int) {
        viewModelScope.launch {
            val flightTime = withContext(Dispatchers.IO) { getTotalFlightByYearUseCase(year) }

            _logbookState.update { currentState ->
                currentState.copy(
                    totalFlightByYear = currentState.totalFlightByYear.toMutableMap()
                        .apply { put(year, getHoursAndMinutes(flightTime.flight)) },
                    totalBlockByYear = currentState.totalBlockByYear.toMutableMap()
                        .apply { put(year, getHoursAndMinutes(flightTime.block)) },
                    totalNightByYear = currentState.totalNightByYear.toMutableMap()
                        .apply { put(year, getHoursAndMinutes(flightTime.night)) }
                )
            }
        }
    }

    /**
     * Calls function [LogbookRepositoryImpl.getTotalFlightWithYear()]
     * [ru.skypaws.data.repository.LogbookRepositoryImpl.getTotalFlightWithYear]
     * to get logbook data [FlightTime][ru.skypaws.domain.model.FlightTime]
     * (flight, block and night time until current year including) from local database.
     *
     * Function changes properties of [logbookState][_logbookState] in [LogbookViewModel]
     * - **totalFlightWithYear** is updated by adding a new pair<year, flight time>
     * - **totalBlockWithYear** is updated by adding a new pair<year, block time>
     * - **totalNightWithYear** is updated by adding a new pair<year, night time>
     * @param [year] year of [Int] type for which data is calculated.
     * @see [getHoursAndMinutes]
     */
    fun getTotalWithYear(year: Int) {
        viewModelScope.launch {
            val totalFlightWithYear =
                withContext(Dispatchers.IO) { getTotalFlightWithYearUseCase(year) }

            _logbookState.update { currentState ->
                currentState.copy(
                    totalFlightWithYear = currentState.totalFlightWithYear.toMutableMap()
                        .apply { put(year, getHoursAndMinutes(totalFlightWithYear.flight)) },
                    totalBlockWithYear = currentState.totalBlockWithYear.toMutableMap()
                        .apply { put(year, getHoursAndMinutes(totalFlightWithYear.block)) },
                    totalNightWithYear = currentState.totalNightWithYear.toMutableMap()
                        .apply { put(year, getHoursAndMinutes(totalFlightWithYear.night)) }
                )
            }
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
        return String.format(Locale(UTC.toString()), "%02d:%02d", hours, minutes)
    }

    /**
     * Transforms ISO date type to day of week (number) type.
     * @param [date] ISO date type
     * @return [Int] [1..7] or 100 if [date] is null
     */
    fun getDayOfWeek(date: String): Int {
        return dateUtils.getDayOfWeek(date)
    }

    /**
     * Transforms ISO date type to dd type.
     * @param [date] ISO date type
     * @return [Int] [1..31] or "" if [date] is null
     */
    fun getDateNumberFromISO(date: String): String {
        return dateUtils.getDateNumberFromISO(date)
    }
}

