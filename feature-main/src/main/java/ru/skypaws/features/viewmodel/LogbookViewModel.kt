package ru.skypaws.features.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.data.utils.DateUtils
import ru.skypaws.mobileapp.domain.model.YearMonth
import ru.skypaws.mobileapp.domain.usecase.logbook.GetAllLogbookDataUseCase
import ru.skypaws.mobileapp.domain.usecase.logbook.GetFlightListByYearMonthUseCase
import ru.skypaws.mobileapp.domain.usecase.logbook.GetMonthTypeListByCurrentYearUseCase
import ru.skypaws.mobileapp.domain.usecase.logbook.GetTotalTimeByYearUseCase
import ru.skypaws.mobileapp.domain.usecase.logbook.GetTotalTimeWithYearUseCase
import ru.skypaws.features.model.LogbookModel
import java.time.ZoneOffset.UTC
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val getAllLogbookDataUseCase: GetAllLogbookDataUseCase,
    private val getMonthTypeListByCurrentYearUseCase: GetMonthTypeListByCurrentYearUseCase,
    private val getFlightListByYearMonthUseCase: GetFlightListByYearMonthUseCase,
    private val getTotalTimeByYearUseCase: GetTotalTimeByYearUseCase,
    private val getTotalTimeWithYearUseCase: GetTotalTimeWithYearUseCase,
) : ViewModel() {
    private val _logbookState = MutableStateFlow(ru.skypaws.features.model.LogbookModel())
    val logbookState: StateFlow<ru.skypaws.features.model.LogbookModel> = _logbookState.asStateFlow()

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
     *
     * In case of Exception() sets [LogbookModel(error = true)][_logbookState]
     */
    fun loadInitialData() {
        _logbookState.update { ru.skypaws.features.model.LogbookModel(error = false) }
        viewModelScope.launch {
            try {
                val logbookDataModel = getAllLogbookDataUseCase()

                _logbookState.update {
                    it.copy(
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
            } catch (e: Exception) {
                _logbookState.update { ru.skypaws.features.model.LogbookModel(error = true) }
            }
        }
    }

    /**
     * Gets list of months with type [MonthType][ru.skypaws.mobileapp.model.MonthType]
     * for a specific [year] by calling function [LogbookRepositoryImpl.getYearMonthTypeListByCurrentYear(year)]
     * [ru.skypaws.mobileapp.repository.LogbookRepositoryImpl.getMonthTypeListByCurrentYear].
     *
     * Updates [logbookState.monthList][_logbookState] in [LogbookViewModel]
     * with new pair<[year], List<[MonthType][ru.skypaws.mobileapp.model.MonthType]>>
     * @param [year] year for which data is calculated.
     */
    fun getMonthList(year: Int) {
        viewModelScope.launch {
            val monthList = getMonthTypeListByCurrentYearUseCase(year)

            _logbookState.update {
                it.copy(
                    monthList = it.monthList + (year to monthList)
                )
            }

        }
    }

    /**
     * Calls function [LogbookRepositoryImpl.getFlightListByYearMonth()]
     * [ru.skypaws.mobileapp.repository.LogbookFlightRepositoryImpl.getFlightListByYearMonth]
     * to get logbook flight data [LogbookFlight][ru.skypaws.mobileapp.model.LogbookFlight]
     * for a specific [year and month][yearMonth].
     *
     * Updates [logbookState.flightList][_logbookState] in [LogbookViewModel]
     * with new pair<[yearMonth], List<[LogbookFlight][ru.skypaws.mobileapp.model.LogbookFlight]>>
     * @param [yearMonth] year and month for which data is calculated.
     */
    fun getFlightList(yearMonth: YearMonth) {
        viewModelScope.launch {
            val flightList = getFlightListByYearMonthUseCase(yearMonth)

            _logbookState.update {
                it.copy(
                    flightList = it.flightList + (yearMonth to flightList)
                )
            }

        }
    }

    /**
     * Gets logbook data [FlightTime][ru.skypaws.mobileapp.model.FlightTime]
     * for a specific [year] calling function [LogbookRepositoryImpl.getTotalFlightByYear()]
     * [ru.skypaws.mobileapp.repository.LogbookRepositoryImpl.getTotalTimeByYear].
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
            val flightTime = getTotalTimeByYearUseCase(year)

            _logbookState.update {
                it.copy(
                    totalFlightByYear = it.totalFlightByYear
                            + (year to getHoursAndMinutes(flightTime.flight)),
                    totalBlockByYear = it.totalBlockByYear
                            + (year to getHoursAndMinutes(flightTime.block)),
                    totalNightByYear = it.totalNightByYear
                            + (year to getHoursAndMinutes(flightTime.night))
                )
            }
        }
    }

    /**
     * Gets logbook data [FlightTime][ru.skypaws.mobileapp.model.FlightTime]
     * (flight, block and night time until current year including) by calling function
     * [LogbookRepositoryImpl.getTotalFlightWithYear()]
     * [ru.skypaws.mobileapp.repository.LogbookRepositoryImpl.getTotalTimeWithYear].
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
            val totalFlightWithYear = getTotalTimeWithYearUseCase(year)

            _logbookState.update {
                it.copy(
                    totalFlightWithYear = it.totalFlightWithYear
                            + (year to getHoursAndMinutes(totalFlightWithYear.flight)),
                    totalBlockWithYear = it.totalBlockWithYear
                            + (year to getHoursAndMinutes(totalFlightWithYear.block)),
                    totalNightWithYear = it.totalNightWithYear
                            + (year to getHoursAndMinutes(totalFlightWithYear.night))
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
    fun getDayOfWeek(date: String): Int = DateUtils.getDayOfWeek(date)


    /**
     * Transforms ISO date type to dd type.
     * @param [date] ISO date type
     * @return [Int] [1..31] or "" if [date] is null
     */
    fun getDateNumberFromISO(date: String): String = DateUtils.getDateNumberFromISO(date)

}

