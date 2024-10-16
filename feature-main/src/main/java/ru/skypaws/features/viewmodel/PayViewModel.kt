package ru.skypaws.features.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.domain.usecase.userPayInfo.GetPayInfoUseCase
import ru.skypaws.features.model.PaidServicesState
import java.time.Instant

abstract class PayViewModel(
    private val getPayInfoUseCase: GetPayInfoUseCase
) : ViewModel() {
    protected val payMutableState = MutableStateFlow(ru.skypaws.features.model.PaidServicesState())
    val payState: StateFlow<ru.skypaws.features.model.PaidServicesState> = payMutableState.asStateFlow()

    /**
     * Gets user pay information from server and saves it locally, changing [payMutableState].
     *
     * Sets [mutableState.refreshing][payMutableState] in [PayViewModel] to **true**.
     *
     * Calls function [UserPayInfoRepository.getPayInfo()][ru.skypaws.mobileapp.repository.user.UserPayInfoRepository.fetchPayInfoAndSave]
     * to get [PayInfo][ru.skypaws.mobileapp.model.dto.PayInfoDto] from server and save it locally.
     *
     * In case of success: uses function [defaultState()][defaultState] to set default state value for [payMutableState].
     *
     * In case of Exception: [networkError][payMutableState] = **true**,
     * [refreshing][payMutableState] = **false**.
     *
     * @param defaultState function to set state to default value
     */
    protected fun checkPayment(defaultState: () -> Unit) {
        payMutableState.update { it.copy(refreshing = true) }
        viewModelScope.launch {
            try {
                getPayInfoUseCase()

                // setting default state for UI
                defaultState()
            } catch (e: Exception) {
                payMutableState.update { it.copy(networkError = true, refreshing = false) }
            }
        }
    }

    /**
     * Checks whether a service is paid and valid.
     * @return **true**, if expirationDate >= current time
     * @param expirationDate [Long] (Logbook or Calendar expiration date)
     */
    fun paidStatus(expirationDate: Long): Boolean {
        val timeNow = Instant.now().epochSecond

        return (expirationDate >= timeNow)
    }
}

