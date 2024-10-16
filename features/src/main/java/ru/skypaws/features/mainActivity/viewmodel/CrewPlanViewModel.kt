package ru.skypaws.features.mainActivity.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.data.error.ApiErrors
import ru.skypaws.data.utils.DateUtils
import ru.skypaws.domain.usecase.crewPlan.CheckForNewCodeUseCase
import ru.skypaws.domain.usecase.crewPlan.GetCrewPlanUseCase
import ru.skypaws.domain.usecase.crewPlan.ObserveCrewPlanDataUseCase
import ru.skypaws.domain.usecase.logbook.GetLatestMonthUseCase
import ru.skypaws.domain.usecase.logbook.GetLatestYearUseCase
import ru.skypaws.domain.usecase.logbook.GetLogbookItemDetailedUseCase
import ru.skypaws.domain.usecase.logbook.GetLogbookUseCase
import ru.skypaws.domain.usecase.logbook.GetYearsAndMonthsByCurrentUseCase
import ru.skypaws.domain.usecase.serviceData.GetPriceInfoFromServiceUseCase
import ru.skypaws.features.mainActivity.model.CrewPlanState
import ru.skypaws.features.model.LoadingDataState
import ru.skypaws.features.viewmodel.LoadingViewModel
import javax.inject.Inject

@HiltViewModel
class CrewPlanViewModel @Inject constructor(
    private val dateUtils: DateUtils,

    private val getLogbookUseCase: GetLogbookUseCase,
    private val checkForNewCodeUseCase: CheckForNewCodeUseCase,
    private val observeCrewPlanDataUseCase: ObserveCrewPlanDataUseCase,
    private val getLogbookItemDetailedUseCase: GetLogbookItemDetailedUseCase,
    private val getYearsAndMonthsByCurrentUseCase: GetYearsAndMonthsByCurrentUseCase,
    private val getLatestMonthUseCase: GetLatestMonthUseCase,
    private val getLatestYearUseCase: GetLatestYearUseCase,

    getCrewPlanUseCase: GetCrewPlanUseCase,
    getPriceInfoFromServiceUseCase: GetPriceInfoFromServiceUseCase,
) : LoadingViewModel(
    getCrewPlanUseCase = getCrewPlanUseCase,
    getPriceInfoFromServiceUseCase = getPriceInfoFromServiceUseCase,
) {
    // state for crewPlan flights and refresh
    private val _crewPlanState = MutableStateFlow(
        CrewPlanState(isNewCode = checkForNewCodeUseCase())
    )
    val crewPlanState: StateFlow<CrewPlanState> = _crewPlanState.asStateFlow()

    init {
        viewModelScope.launch {
            observeCrewPlanDataUseCase().collect { flights ->
                _crewPlanState.value = _crewPlanState.value.copy(
                    flights = flights,
                )
            }
        }
    }

    /**
     * Sets in [CrewPlanViewModel] property [loadingMutableState][loadingMutableState] as:
     * - [crewPlanLoaded][loadingMutableState] copied,
     * - [logbookLoaded][loadingMutableState] copied,
     * - [loadingAviabitData][loadingMutableState] to **true**
     */
    fun loadingAviabitData() {
        loadingMutableState.value = LoadingDataState(
            crewPlanLoaded = loadingMutableState.value.crewPlanLoaded,
            logbookLoaded = loadingMutableState.value.logbookLoaded,
            loadingAviabitData = true
        )
    }

    /**
     * Changes properties of [loadingMutableState][loadingMutableState] in [CrewPlanViewModel]
     *
     * If [crewPlanLoaded][loadingMutableState] and [logbookLoaded][loadingMutableState] ->
     * set to initial state [LoadingDataState()][LoadingDataState]
     *
     * else [loadingAviabitData][loadingMutableState] and
     * [loginAviabitPage][loadingMutableState] = **false**, other properties are copied
     */
    fun loadingDataStopped() {
        if (loadingMutableState.value.crewPlanLoaded && loadingMutableState.value.logbookLoaded) {
            loadingMutableState.value = LoadingDataState()
        } else {
            loadingMutableState.update { currentState ->
                currentState.copy(
                    loadingAviabitData = false,
                    loginAviabitPage = false
                )
            }
        }
    }

    /**
     * Gets new flight data from previous month and year from max in database until the latest from server
     * (updates previous and current month and gets new data).
     *
     * Calls function [LogbookRepositoryImpl.getLatestMonth()][ru.skypaws.data.repository.LogbookRepositoryImpl.getLatestMonth]
     * to get latest month from database.
     *
     * Sets to previous month 12, if latest month == 1, or latest month - 1.
     *
     * Calls function [LogbookRepositoryImpl.getLatestYear()][ru.skypaws.data.repository.LogbookRepositoryImpl.getLatestYear]
     * to get latest year from database.
     *
     * Sets to previous year year - 1, if latest month == 1, or year.
     *
     * Calls function [LogbookRepositoryImpl.getLogbook()][ru.skypaws.data.repository.LogbookRepositoryImpl.getLogbook]
     * to get logbook data [List(LogbookDto)][ru.skypaws.data.source.dto.LogbookDto] from server.
     *
     * Calls function [LogbookRepositoryImpl.getYearsAndMonthsByCurrent(year, month)]
     * [ru.skypaws.data.repository.LogbookRepositoryImpl.getYearsAndMonthsByCurrent]
     * to get logbook data [List(LogbookDto)][ru.skypaws.data.source.dto.LogbookDto] from database.
     *
     * Calls [LogbookRepositoryImpl.getLogbookItemDetailed(year, month)]
     * [ru.skypaws.data.repository.LogbookRepositoryImpl.getLogbookItemDetailed]
     * to get data for each flight [List(LogbookFlightDto)][ru.skypaws.data.source.dto.LogbookFlightDto] from server
     *
     * Sets [loadingMutableState.logbookLoaded][loadingMutableState] in [CrewPlanViewModel] to **true**
     *
     * In case of exception:
     * - ApiErrors: [aviabitServerTimeOut][loadingMutableState] = **true**, if code = 0:
     * [loginAviabitPage][loadingMutableState] = **true**
     * - Exception: [errorToLoadData][loadingMutableState] = **true**
     */
    fun getLogbookDetailedLastMonth() {
        viewModelScope.launch {
            try {
                val maxMonth = getLatestMonthUseCase()
                val previousMonth = if (maxMonth == 1) {
                    12
                } else {
                    maxMonth - 1
                }
                val year = getLatestYearUseCase()
                val yearPrevMonth = if (maxMonth == 1) {
                    year - 1
                } else {
                    year
                }

                getLogbookUseCase()

                val logbookItems = getYearsAndMonthsByCurrentUseCase(
                    yearPrevMonth,
                    previousMonth
                )
                logbookItems.forEach {
                    getLogbookItemDetailedUseCase(it.year, it.month)
                }

                loadingMutableState.update { it.copy(logbookLoaded = true) }
            } catch (e: ApiErrors) {
                if (e.code == 0) {
                    loadingMutableState.update {
                        it.copy(
                            loginAviabitPage = true,
                            error = true,
                            logbookError = true,
                            aviabitServerTimeOut = true
                        )
                    }
                } else
                    loadingMutableState.update {
                        it.copy(
                            error = true,
                            logbookError = true,
                            aviabitServerTimeOut = true
                        )
                    }
            } catch (e: Exception) {
                loadingMutableState.update {
                    it.copy(
                        error = true,
                        logbookError = true,
                        aviabitServerTimeOut = true
                    )
                }
            }
        }
    }

    /**
     * Call function [CrewPlanRepositoryImpl.getCrewPlan()][ru.skypaws.data.repository.CrewPlanRepositoryImpl.getCrewPlan]
     * to get crew plan data from server
     *
     * Function changes properties of [crewPlanState][_crewPlanState] in [CrewPlanViewModel]
     * - In case there's successful response: [refreshFinished][_crewPlanState] = **true**
     * - In case there's exception: [refreshFinished][_crewPlanState] and
     * [refreshError][_crewPlanState] = **true**
     *      - ApiErrors: [aviabitServerTimeOut][loadingMutableState] = **true**
     *      - Exception: [refreshErrorOfResponse][loadingMutableState] = **true**
     */
    fun refresh() {
        _crewPlanState.value = _crewPlanState.value.copy(
            refreshFinished = false,
            refreshError = false,
            refreshErrorOfResponse = false,
            isNewCode = false
        )
        viewModelScope.launch {
            try {
                getCrewPlanUseCase()

                _crewPlanState.update { it.copy(refreshFinished = true) }
            } catch (e: ApiErrors) {
                _crewPlanState.update {
                    it.copy(
                        refreshFinished = true,
                        refreshError = true,
                        refreshErrorOfResponse = true
                    )
                }
            } catch (e: Exception) {
                _crewPlanState.update {
                    it.copy(
                        refreshFinished = true,
                        refreshError = true
                    )
                }
            }
        }
    }

    fun getDateFromISO(date: String?): String {
        return dateUtils.getDateFromISO(date)
    }

    fun getTimeFromISO(date: String?): String {
        return dateUtils.getTimeFromISO(date)
    }

    fun getTimeBetween(dateFrom: String?, dateUntil: String?): String {
        return dateUtils.getTimeBetween(dateFrom, dateUntil)
    }

    fun getISOTimeLandingCalculated(date: String?, dateFrom: String?, dateUntil: String?): String {
        return dateUtils.getISOTimeLandingCalculated(date, dateFrom, dateUntil)
    }

    fun getTimeLandingCalculated(date: String?, dateFrom: String?, dateUntil: String?): String {
        return dateUtils.getTimeLandingCalculated(date, dateFrom, dateUntil)
    }

    fun addMinutesToTime(date: String?, minutes: Long): String {
        return dateUtils.addMinutesToTime(date, minutes)
    }
}