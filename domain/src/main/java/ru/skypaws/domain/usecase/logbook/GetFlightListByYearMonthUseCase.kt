package ru.skypaws.domain.usecase.logbook

import ru.skypaws.domain.model.LogbookFlight
import ru.skypaws.domain.repository.LogbookRepository
import javax.inject.Inject

class GetFlightListByYearMonthUseCase @Inject constructor(
    private val repository: LogbookRepository
) {
    suspend operator fun invoke(year: Int, month: Int): List<LogbookFlight> {
        return repository.getFlightListByYearMonth(year, month)
    }
}