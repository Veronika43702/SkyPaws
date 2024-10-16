package ru.skypaws.features.enterActivity.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.skypaws.data.error.ApiErrors
import ru.skypaws.domain.usecase.crewPlan.GetCrewPlanUseCase
import ru.skypaws.domain.usecase.logbook.GetLogbookDetailedUseCase
import ru.skypaws.domain.usecase.logbook.GetLogbookItemDetailedUseCase
import ru.skypaws.domain.usecase.logbook.GetLogbookUseCase
import ru.skypaws.domain.usecase.serviceData.GetPriceInfoFromServiceUseCase
import ru.skypaws.features.model.LoadingDataState
import ru.skypaws.features.viewmodel.LoadingViewModel
import javax.inject.Inject

@HiltViewModel
class EnterLoadingViewModel @Inject constructor(
    private val getLogbookUseCase: GetLogbookUseCase,
    private val getLogbookItemDetailedUseCase: GetLogbookItemDetailedUseCase,
    private val getLogbookDetailedUseCase: GetLogbookDetailedUseCase,

    getCrewPlanUseCase: GetCrewPlanUseCase,
    getPriceInfoFromServiceUseCase: GetPriceInfoFromServiceUseCase,
) : LoadingViewModel(
    getCrewPlanUseCase = getCrewPlanUseCase,
    getPriceInfoFromServiceUseCase = getPriceInfoFromServiceUseCase,
) {
    /**
     * Sets [loadingAviabitData][loadingMutableState] in [EnterLoadingViewModel] to **true** and
     * [loadingState][loadingState] to initial state
     * [LoadingDataState()][LoadingDataState]
     */
    fun loadingData() {
        loadingMutableState.value = LoadingDataState(
            crewPlanLoaded = loadingMutableState.value.crewPlanLoaded,
            logbookLoaded = loadingMutableState.value.logbookLoaded,
            loadingAviabitData = true
        )
        loadingMutableState.value = LoadingDataState()
    }

    /**
     * Changes properties of [loadingMutableState][loadingMutableState] in [EnterLoadingViewModel]
     *
     * If [crewPlanLoaded][loadingMutableState] and [logbookLoaded][loadingMutableState] ->
     * set to initial state [LoadingDataState()][LoadingDataState]
     *
     * else [loadingAviabitData][loadingMutableState] = **false**, other properties are copied
     */
    fun loadingDataStopped() {
        if (loadingMutableState.value.crewPlanLoaded && loadingMutableState.value.logbookLoaded) {
            loadingMutableState.value = LoadingDataState()
        } else {
            loadingMutableState.value = loadingMutableState.value.copy(loadingAviabitData = false)
        }
    }

    /**
     * Call functions synchronized:
     * - [LogbookRepositoryImpl.getLogbook()][ru.skypaws.data.repository.LogbookRepositoryImpl.getLogbook]
     * to get logbook data [List(LogbookDto)][ru.skypaws.data.source.dto.LogbookDto] from server
     * - [LogbookRepositoryImpl.getLogbookDetailed()][ru.skypaws.data.repository.LogbookRepositoryImpl.getLogbookDetailed]
     * to get logbook data [List(YearMonth)][ru.skypaws.domain.model.YearMonth] from local data base
     * - [LogbookRepositoryImpl.getLogbookItemDetailed(year, month)][ru.skypaws.data.repository.LogbookRepositoryImpl.getLogbookItemDetailed]
     * to get data for each flight [List(LogbookFlightDto)][ru.skypaws.data.source.dto.LogbookFlightDto] from server
     *
     * Function changes properties of [loadingMutableState][loadingMutableState] in [EnterLoadingViewModel]
     * - In case there's successful response: [logbookLoaded][loadingMutableState] = **true**
     * - In case there's exception: [error][loadingMutableState] and
     * [logbookError][loadingMutableState] = **true**
     *      - ApiErrors: [aviabitServerTimeOut][loadingMutableState] = **true**
     *      - Exception: [errorToLoadData][loadingMutableState] = **true**
     */
    fun getLogbookFlights() {
        viewModelScope.launch {
            try {
                getLogbookUseCase()

                val logbookItems = getLogbookDetailedUseCase()
                logbookItems.forEach {
                    getLogbookItemDetailedUseCase(it.year, it.month)
                }

                loadingMutableState.value = loadingMutableState.value.copy(logbookLoaded = true)
            }  catch (e: ApiErrors) {
                loadingMutableState.value = loadingMutableState.value.copy(
                    error = true,
                    logbookError = true,
                    aviabitServerTimeOut = true
                )
            } catch (e: Exception) {
                loadingMutableState.value =
                    loadingMutableState.value.copy(
                        error = true,
                        logbookError = true,
                        errorToLoadData = true
                    )
            }
        }
    }
}