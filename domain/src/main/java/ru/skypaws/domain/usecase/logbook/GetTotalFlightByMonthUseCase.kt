package ru.skypaws.domain.usecase.logbook

import ru.skypaws.domain.model.FlightTime
import ru.skypaws.domain.repository.LogbookRepository
import javax.inject.Inject

class GetTotalFlightByMonthUseCase @Inject constructor(
    private val repository: LogbookRepository
) {
    suspend operator fun invoke(year: Int, month: Int): FlightTime {
        return repository.getTotalFlightByMonth(year, month)
    }
}