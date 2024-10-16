package ru.skypaws.mobileapp.domain.usecase.logbook

import ru.skypaws.mobileapp.domain.model.LogbookFlight
import ru.skypaws.mobileapp.domain.model.YearMonth
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookAndFlightRepository
import javax.inject.Inject

class GetFlightListByYearMonthUseCase @Inject constructor(
    private val repository: LogbookAndFlightRepository
) {
    suspend operator fun invoke(yearMonth: YearMonth): List<LogbookFlight> {
        return repository.getFlightListByYearMonth(yearMonth)
    }
}