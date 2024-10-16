package ru.skypaws.features.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.data.utils.DateUtils
import ru.skypaws.mobileapp.domain.usecase.serviceData.GetCalendarMonthPriceUseCase
import ru.skypaws.mobileapp.domain.usecase.serviceData.GetCalendarQuarterPriceUseCase
import ru.skypaws.mobileapp.domain.usecase.serviceData.GetCalendarYearPriceUseCase
import ru.skypaws.mobileapp.domain.usecase.serviceData.GetLogbookPriceUseCase
import ru.skypaws.mobileapp.domain.usecase.userPayInfo.GetCalendarExpDateUseCase
import ru.skypaws.mobileapp.domain.usecase.userPayInfo.GetLogbookExpDateUseCase
import ru.skypaws.mobileapp.domain.usecase.userPayInfo.GetPayInfoUseCase
import ru.skypaws.features.model.PaidServicesState
import javax.inject.Inject

sealed class PaidServicesIntent {
    data object DefaultState : PaidServicesIntent()
    data object Refresh : PaidServicesIntent()
}

@HiltViewModel
class PaidServicesViewModel @Inject constructor(
    private val getLogbookExpDateUseCase: GetLogbookExpDateUseCase,
    private val getLogbookPriceUseCase: GetLogbookPriceUseCase,
    private val getCalendarMonthPriceUseCase: GetCalendarMonthPriceUseCase,
    private val getCalendarQuarterPriceUseCase: GetCalendarQuarterPriceUseCase,
    private val getCalendarYearPriceUseCase: GetCalendarYearPriceUseCase,
    private val getCalendarExpDateUseCase: GetCalendarExpDateUseCase,
    getPayInfoUseCase: GetPayInfoUseCase
) : PayViewModel(getPayInfoUseCase = getPayInfoUseCase) {
    fun handleIntent(intent: PaidServicesIntent) {
        when (intent) {
            is PaidServicesIntent.DefaultState -> {
                updateStateByData()
            }

            is PaidServicesIntent.Refresh -> {
                checkPayment { handleIntent(PaidServicesIntent.DefaultState) }
            }
        }
    }

    private fun updateStateByData() {
        viewModelScope.launch {
            payMutableState.update {
                ru.skypaws.features.model.PaidServicesState(
                    isLogbookPaid = paidStatus(getLogbookExpDateUseCase()),
                    logbookPrice = getLogbookPriceUseCase(),
                    logbookExpDate = DateUtils.getDate(getLogbookExpDateUseCase()),

                    isCalendarPaid = paidStatus(getCalendarExpDateUseCase()),
                    calendarMonthPrice = getCalendarMonthPriceUseCase(),
                    calendarQuarterPrice = getCalendarQuarterPriceUseCase(),
                    calendarYearPrice = getCalendarYearPriceUseCase(),
                    calendarExpDate = DateUtils.getDate(getCalendarExpDateUseCase()),
                )
            }
        }
    }
}

