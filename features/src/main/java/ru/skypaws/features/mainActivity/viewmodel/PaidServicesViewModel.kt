package ru.skypaws.features.mainActivity.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.skypaws.data.utils.DateUtils
import ru.skypaws.domain.usecase.serviceData.GetCalendarMonthPriceUseCase
import ru.skypaws.domain.usecase.serviceData.GetCalendarQuarterPriceUseCase
import ru.skypaws.domain.usecase.serviceData.GetCalendarYearPriceUseCase
import ru.skypaws.domain.usecase.serviceData.GetLogbookPriceUseCase
import ru.skypaws.domain.usecase.userPayInfo.GetCalendarExpDateUseCase
import ru.skypaws.domain.usecase.userPayInfo.GetLogbookExpDateUseCase
import ru.skypaws.domain.usecase.userPayInfo.GetPayInfoUseCase
import ru.skypaws.features.mainActivity.model.PaidServicesState
import ru.skypaws.features.mainActivity.viewmodel.abstracts.PayViewModel
import javax.inject.Inject

sealed class PaidServicesIntent {
    data object DefaultState : PaidServicesIntent()
    data object Refresh : PaidServicesIntent()
}

@HiltViewModel
class PaidServicesViewModel @Inject constructor(
    private val dateUtils: DateUtils,
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
                mutableState.value = PaidServicesState(
                    isLogbookPaid = paidStatus(getLogbookExpDateUseCase()),
                    logbookPrice = getLogbookPriceUseCase(),
                    logbookExpDate = dateUtils.getDate(getLogbookExpDateUseCase()),

                    isCalendarPaid = paidStatus(getCalendarExpDateUseCase()),
                    calendarMonthPrice = getCalendarMonthPriceUseCase(),
                    calendarQuarterPrice = getCalendarQuarterPriceUseCase(),
                    calendarYearPrice = getCalendarYearPriceUseCase(),
                    calendarExpDate = dateUtils.getDate(getCalendarExpDateUseCase()),
                )
            }

            is PaidServicesIntent.Refresh -> {
                checkPayment { handleIntent(PaidServicesIntent.DefaultState) }
            }
        }
    }
}

