package ru.skypaws.features.enterActivity.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.domain.usecase.crewPlan.GetCrewPlanUseCase
import ru.skypaws.mobileapp.domain.usecase.logbook.FetchLogbookAndAllFlightsUseCase
import ru.skypaws.mobileapp.domain.usecase.serviceData.GetPriceInfoFromServiceUseCase
import ru.skypaws.presentation.model.LoadingDataState
import ru.skypaws.presentation.viewmodel.LoadingViewModel
import javax.inject.Inject

@HiltViewModel
class EnterLoadingViewModel @Inject constructor(
    private val fetchLogbookAndAllFlightsUseCase: FetchLogbookAndAllFlightsUseCase,

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
        loadingMutableState.update {
            LoadingDataState(
                crewPlanLoaded = loadingMutableState.value.crewPlanLoaded,
                logbookLoaded = loadingMutableState.value.logbookLoaded,
                loadingAviabitData = true
            )
        }
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
            loadingMutableState.update { LoadingDataState() }
        } else {
            loadingMutableState.update { it.copy(loadingAviabitData = false) }
        }
    }

    /**
     * Sets [loadingAviabitData][loadingMutableState] to initial state
     * [LoadingDataState()][LoadingDataState]
     */
    fun resetState() {
        loadingMutableState.update { LoadingDataState() }
    }

    /**
     * Fetches logbook and logbook flight data from server.
     *
     * Function changes properties of [loadingMutableState][loadingMutableState] in [EnterLoadingViewModel]
     * - In case there's successful response: [logbookLoaded][loadingMutableState] = **true**
     * - In case there's exception: [error][loadingMutableState] and
     * [logbookError][loadingMutableState] = **true**
     *      - ApiErrors: [aviabitServerTimeOut][loadingMutableState] = **true**
     *      - Exception: [errorToLoadData][loadingMutableState] = **true**
     */
    fun fetchLogbookAndLogbookFlightsFromServer() {
        viewModelScope.launch {
            try {
                // TODO fetchLogbookAndAllFlightsUseCase()

                loadingMutableState.update { it.copy(logbookLoaded = true) }
            } catch (e: ApiErrors) {
                if (e.code == 406) {
                    loadingMutableState.update {
                        it.copy(
                            loginAviabitPage = true,
                            error = true,
                            logbookError = true,
                            aviabitServerTimeOut = true
                        )
                    }
                } else {
                    loadingMutableState.update {
                        it.copy(
                            error = true,
                            logbookError = true,
                            aviabitServerTimeOut = true
                        )
                    }
                }
            } catch (e: Exception) {
                loadingMutableState.update {
                    it.copy(
                        error = true,
                        logbookError = true,
                        errorToLoadData = true
                    )
                }
            }
        }
    }
}