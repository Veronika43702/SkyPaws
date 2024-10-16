package ru.skypaws.features.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.domain.usecase.serviceData.GetLogbookPriceUseCase
import ru.skypaws.mobileapp.domain.usecase.userPayInfo.GetPayInfoUseCase
import ru.skypaws.features.model.PaidServicesState
import javax.inject.Inject

sealed class LogbookPayIntent {
    data object DefaultState : LogbookPayIntent()
    data object Refresh : LogbookPayIntent()
}

@HiltViewModel
class LogbookPayViewModel @Inject constructor(
    private val getLogbookPriceUseCase: GetLogbookPriceUseCase,
    getPayInfoUseCase: GetPayInfoUseCase
) : PayViewModel(getPayInfoUseCase = getPayInfoUseCase) {
    fun handleIntent(intent: LogbookPayIntent) {
        when (intent) {
            is LogbookPayIntent.DefaultState -> {
                updateStateByData()
            }

            is LogbookPayIntent.Refresh -> {
                checkPayment { handleIntent(LogbookPayIntent.DefaultState) }
            }
        }
    }

    private fun updateStateByData() {
        viewModelScope.launch {
            payMutableState.update {
                ru.skypaws.features.model.PaidServicesState(logbookPrice = getLogbookPriceUseCase())
            }
        }
    }
}

