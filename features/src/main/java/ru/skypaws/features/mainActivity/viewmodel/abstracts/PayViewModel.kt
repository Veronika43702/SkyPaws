package ru.skypaws.features.mainActivity.viewmodel.abstracts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.skypaws.domain.usecase.userPayInfo.GetPayInfoUseCase
import ru.skypaws.features.mainActivity.model.PaidServicesState
import java.time.Instant

abstract class PayViewModel(
    private val getPayInfoUseCase: GetPayInfoUseCase
) : ViewModel() {
    protected val mutableState = MutableStateFlow(PaidServicesState())
    val state: StateFlow<PaidServicesState> = mutableState.asStateFlow()

    /**
     * Sets [mutableState.refreshing][mutableState] in [PayViewModel] to **true**.
     *
     * Calls function [UserPayInfoRepositoryImpl.getPayInfo()][ru.skypaws.data.repository.UserPayInfoRepositoryImpl.getPayInfo]
     * to get [PayInfo][ru.skypaws.data.source.dto.PayInfoDto] from server and save it to SharedPreferences.
     *
     * In case of success: uses function [defaultState()][defaultState] to set default state value for [mutableState].
     *
     * In case of Exception: [networkError][mutableState] = **true**,
     * [refreshing][mutableState] = **false**.
     *
     * @param defaultState - function to set state to default value
     */
    protected fun checkPayment(defaultState: () -> Unit) {
        mutableState.value = mutableState.value.copy(refreshing = true)
        viewModelScope.launch {
            try {
                getPayInfoUseCase()

                // setting default state for UI
                defaultState()
            } catch (e: Exception) {
                mutableState.value =
                    mutableState.value.copy(networkError = true, refreshing = false)
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

