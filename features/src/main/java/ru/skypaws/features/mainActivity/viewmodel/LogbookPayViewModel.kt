package ru.skypaws.features.mainActivity.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.skypaws.domain.usecase.serviceData.GetLogbookPriceUseCase
import ru.skypaws.domain.usecase.userPayInfo.GetPayInfoUseCase
import ru.skypaws.features.mainActivity.model.PaidServicesState
import ru.skypaws.features.mainActivity.viewmodel.abstracts.PayViewModel
import javax.inject.Inject

sealed class LogbookPayIntent {
    object DefaultState : LogbookPayIntent()
    object Refresh : LogbookPayIntent()
}

@HiltViewModel
class LogbookPayViewModel @Inject constructor(
    private val getLogbookPriceUseCase: GetLogbookPriceUseCase,
    getPayInfoUseCase: GetPayInfoUseCase
) : PayViewModel(getPayInfoUseCase = getPayInfoUseCase) {
    fun handleIntent(intent: LogbookPayIntent) {
        when (intent) {
            is LogbookPayIntent.DefaultState -> {
                mutableState.value = PaidServicesState(
                    logbookPrice = getLogbookPriceUseCase()
                )
            }

            is LogbookPayIntent.Refresh -> {
                checkPayment { handleIntent(LogbookPayIntent.DefaultState) }
            }
        }
    }
}

