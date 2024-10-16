package ru.skypaws.features.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.skypaws.data.error.ApiErrors
import ru.skypaws.domain.usecase.crewPlan.GetCrewPlanUseCase
import ru.skypaws.domain.usecase.serviceData.GetPriceInfoFromServiceUseCase
import ru.skypaws.features.model.LoadingDataState
import java.net.SocketTimeoutException

abstract class LoadingViewModel(
    protected val getCrewPlanUseCase: GetCrewPlanUseCase,
    private val getPriceInfoFromServiceUseCase: GetPriceInfoFromServiceUseCase,
): ViewModel() {
    protected val loadingMutableState = MutableStateFlow(LoadingDataState())
    val loadingState: StateFlow<LoadingDataState> = loadingMutableState.asStateFlow()

    /**
     * Calls function [CrewPlanRepositoryImpl.getCrewPlan()][ru.skypaws.data.repository.CrewPlanRepositoryImpl.getCrewPlan]
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

                loadingMutableState.value = loadingMutableState.value.copy(crewPlanLoaded = true)
            } catch (e: SocketTimeoutException) {
                loadingMutableState.value = loadingMutableState.value.copy(
                    error = true,
                    crewPlanError = true,
                    aviabitServerTimeOut = true
                )
            } catch (e: ApiErrors) {
                if (e.code == 0) {
                    loadingMutableState.value = loadingMutableState.value.copy(
                        loginAviabitPage = true,
                        error = true,
                        crewPlanError = true,
                        aviabitServerTimeOut = true
                    )
                } else
                    loadingMutableState.value = loadingMutableState.value.copy(
                        error = true,
                        crewPlanError = true,
                        aviabitServerTimeOut = true
                    )
            } catch (e: Exception) {
                loadingMutableState.value = loadingMutableState.value.copy(
                    error = true,
                    crewPlanError = true,
                    errorToLoadData = true
                )
            }
        }
    }

    /**
     * Calls function [ServiceDataRepositoryImpl.getPriceInfoFromService()][ru.skypaws.data.repository.ServiceDataRepositoryImpl.getPriceInfoFromService]
     * to get user [PricesDto][ru.skypaws.data.source.dto.PricesDto] from server
     */
    fun getPriceInfo() {
        viewModelScope.launch {
            getPriceInfoFromServiceUseCase()
        }
    }
}