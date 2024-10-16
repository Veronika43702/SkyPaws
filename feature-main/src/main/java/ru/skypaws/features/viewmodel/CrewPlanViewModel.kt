package ru.skypaws.features.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.data.utils.DateUtils
import ru.skypaws.mobileapp.domain.usecase.settings.airportCode.CheckForNewCodeUseCase
import ru.skypaws.mobileapp.domain.usecase.crewPlan.GetCrewPlanUseCase
import ru.skypaws.mobileapp.domain.usecase.crewPlan.ObserveCrewPlanDataUseCase
import ru.skypaws.mobileapp.domain.usecase.logbook.FetchAllOrUpdateLogbookAndFlightsFromServerUseCase
import ru.skypaws.mobileapp.domain.usecase.serviceData.GetPriceInfoFromServiceUseCase
import ru.skypaws.features.model.CrewPlanState
import ru.skypaws.presentation.model.LoadingDataState
import ru.skypaws.presentation.viewmodel.LoadingViewModel
import javax.inject.Inject

@HiltViewModel
class CrewPlanViewModel @Inject constructor(
    private val checkForNewCodeUseCase: CheckForNewCodeUseCase,
    private val observeCrewPlanDataUseCase: ObserveCrewPlanDataUseCase,
    private val fetchAllOrUpdateLogbookAndFlightsFromServerUseCase: FetchAllOrUpdateLogbookAndFlightsFromServerUseCase,

    getCrewPlanUseCase: GetCrewPlanUseCase,
    getPriceInfoFromServiceUseCase: GetPriceInfoFromServiceUseCase,
) : LoadingViewModel(
    getCrewPlanUseCase = getCrewPlanUseCase,
    getPriceInfoFromServiceUseCase = getPriceInfoFromServiceUseCase,
) {
    private val _crewPlanState = MutableStateFlow(ru.skypaws.features.model.CrewPlanState())
    val crewPlanState: StateFlow<ru.skypaws.features.model.CrewPlanState> = _crewPlanState.asStateFlow()

    private var isCrewPlanFlowStopped = false

    /**
     * Starts to collect flights from local storage and
     * update [flights][ru.skypaws.mobileapp.model.CrewPlanEvent] property of
     * [_crewPlanState][_crewPlanState].
     *
     * In case of exception retries 3 times to get data. If failed 3 times:
     * - catches Exception
     * - emits emptyList()
     * - changes [isCrewPlanFlowStopped] to **true**
     */
    fun startObservingCrewPlanData() {
        viewModelScope.launch {
            observeCrewPlanDataUseCase()
                .retry(3)
                .catch {
                    _crewPlanState.update {
                        it.copy(flights = emptyList())
                    }
                    isCrewPlanFlowStopped = true
                }
                .collect { flights ->
                    _crewPlanState.update {
                        it.copy(flights = flights)
                    }
                }
        }

        checkNewCodeSettings()
    }

    private fun checkNewCodeSettings() {
        viewModelScope.launch {
            _crewPlanState.update { it.copy(isNewCode = checkForNewCodeUseCase()) }
        }
    }

    /**
     * Sets in [CrewPlanViewModel] property [loadingMutableState][loadingMutableState] as:
     * - [crewPlanLoaded][loadingMutableState] copied,
     * - [logbookLoaded][loadingMutableState] copied,
     * - [loadingAviabitData][loadingMutableState] to **true**
     */
    fun loadingAviabitData() {
        loadingMutableState.update {
            LoadingDataState(
                crewPlanLoaded = loadingMutableState.value.crewPlanLoaded,
                logbookLoaded = loadingMutableState.value.logbookLoaded,
                loadingAviabitData = true
            )
        }
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
            loadingMutableState.update { LoadingDataState() }
        } else {
            loadingMutableState.update {
                it.copy(
                    loadingAviabitData = false,
                    loginAviabitPage = false
                )
            }
        }
    }

    /**
     * Updates logbook and flight data from server if local database is not empty or fetch all data by calling
     * [fetchAllOrUpdateLogbookAndFlightsFromServer][ru.skypaws.mobileapp.repository.logbook.LogbookAndFlightRepository.fetchAllOrUpdateLogbookAndFlightsFromServer].
     *
     * In case of success sets [loadingMutableState.logbookLoaded][loadingMutableState] in [CrewPlanViewModel] to **true**
     *
     * In case of exception:
     * - ApiErrors: [aviabitServerTimeOut][loadingMutableState] = **true**, if code = 0:
     * [loginAviabitPage][loadingMutableState] = **true**
     * - Exception: [errorToLoadData][loadingMutableState] = **true**
     */
    fun fetchLogbookDetailedLastMonth() {
        viewModelScope.launch {
            try {
                fetchAllOrUpdateLogbookAndFlightsFromServerUseCase()

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
     * Updates crew plan data from server by calling function
     * [CrewPlanRepositoryImpl.getCrewPlan()][ru.skypaws.mobileapp.repository.CrewPlanRepositoryImpl.fetchCrewPlanFromServer]
     *
     * In case [isCrewPlanFlowStopped] = **true** starts to collect data from local storage and
     * changes [isCrewPlanFlowStopped] to **false**.
     *
     * Function changes properties of [crewPlanState][_crewPlanState] in [CrewPlanViewModel]
     * - In case there's successful response: [refreshFinished][_crewPlanState] = **true**
     * - In case there's exception: [refreshFinished][_crewPlanState] and
     * [refreshError][_crewPlanState] = **true**
     *      - ApiErrors: [aviabitServerTimeOut][loadingMutableState] = **true**
     *      - Exception: [refreshErrorOfResponse][loadingMutableState] = **true**
     */
    fun refresh() {
        _crewPlanState.update {
            it.copy(
                refreshFinished = false,
                refreshError = false,
                refreshErrorOfResponse = false,
                isNewCode = false
            )
        }
        viewModelScope.launch {
            try {
                getCrewPlanUseCase()

                if (isCrewPlanFlowStopped) {
                    startObservingCrewPlanData()
                    isCrewPlanFlowStopped = false
                }

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

    fun getDateFromISO(date: String?): String =
        DateUtils.getDateFromISO(date)


    fun getTimeFromISO(date: String?): String =
        DateUtils.getTimeFromISO(date)


    fun getTimeBetween(dateFrom: String?, dateUntil: String?): String =
        DateUtils.getTimeBetween(dateFrom, dateUntil)


    fun getISOTimeLandingCalculated(date: String?, dateFrom: String?, dateUntil: String?): String =
        DateUtils.getISOTimeLandingCalculated(date, dateFrom, dateUntil)


    fun getTimeLandingCalculated(date: String?, dateFrom: String?, dateUntil: String?): String =
        DateUtils.getTimeLandingCalculated(date, dateFrom, dateUntil)


    fun addMinutesToTime(date: String?, minutes: Long): String =
        DateUtils.addMinutesToTime(date, minutes)

}