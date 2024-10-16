package ru.skypaws.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.data.error.ApiErrors
import ru.skypaws.mobileapp.domain.usecase.crewPlan.GetCrewPlanUseCase
import ru.skypaws.mobileapp.domain.usecase.serviceData.GetPriceInfoFromServiceUseCase
import ru.skypaws.presentation.model.LoadingDataState
import java.net.SocketTimeoutException

abstract class LoadingViewModel(
    protected val getCrewPlanUseCase: GetCrewPlanUseCase,
    private val getPriceInfoFromServiceUseCase: GetPriceInfoFromServiceUseCase,
) : ViewModel() {
    protected val loadingMutableState = MutableStateFlow(LoadingDataState())
    val loadingState: StateFlow<LoadingDataState> = loadingMutableState.asStateFlow()

    /**
     * Calls function [CrewPlanRepositoryImpl.getCrewPlan()][ru.skypaws.mobileapp.repository.CrewPlanRepositoryImpl.fetchCrewPlanFromServer]
     * to get crew plan data from server.
     *
     * Changes properties of [loadingMutableState][loadingMutableState] in [LoadingViewModel]:
     *
     * In case:
     * - there's successful response: [crewPlanLoaded][loadingMutableState] = **true**
     * - there's exception: [error][loadingMutableState] and
     * [CrewPlanError][loadingMutableState] = **true**:
     *      - ApiErrors: [aviabitServerTimeOut][loadingMutableState] = **true**.
     *      - if [ApiErrors.code][ru.skypaws.data.error.ApiErrors] == 0:  [loginAviabitPage][loadingMutableState] = **true**
     *      - Exception: [errorToLoadData][loadingMutableState] = **true**
     */
    fun loadCrewPlan() {
        viewModelScope.launch {
            try {
                getCrewPlanUseCase()

                loadingMutableState.update {
                    it.copy(crewPlanLoaded = true)
                }
            } catch (e: SocketTimeoutException) {
                loadingMutableState.update {
                    it.copy(
                        error = true,
                        crewPlanError = true,
                        aviabitServerTimeOut = true
                    )
                }
            } catch (e: ApiErrors) {
                if (e.code == 406) {
                    loadingMutableState.update {
                        it.copy(
                            loginAviabitPage = true,
                            error = true,
                            crewPlanError = true,
                            aviabitServerTimeOut = true
                        )
                    }
                } else {
                    loadingMutableState.update {
                        it.copy(
                            error = true,
                            crewPlanError = true,
                            aviabitServerTimeOut = true
                        )
                    }
                }
            } catch (e: Exception) {
                loadingMutableState.update {
                    it.copy(
                        error = true,
                        crewPlanError = true,
                        errorToLoadData = true
                    )
                }
            }
        }
    }

    /**
     * Gets user [PricesDto][ru.skypaws.mobileapp.model.dto.PricesDto] data from server by
     * [ServiceDataRepository.getPriceInfoFromService()][ru.skypaws.mobileapp.repository.ServiceDataRepository.fetchPriceInfoFromService]
     */
    fun getPriceInfo() {
        viewModelScope.launch {
            getPriceInfoFromServiceUseCase()
        }
    }
}