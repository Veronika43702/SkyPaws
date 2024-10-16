package ru.skypaws.domain.usecase.logbook

import ru.skypaws.domain.model.FlightTime
import ru.skypaws.domain.repository.LogbookRepository
import javax.inject.Inject

class GetTotalFlightByYearUseCase @Inject constructor(
    private val repository: LogbookRepository
) {
    suspend operator fun invoke(year: Int): FlightTime {
        return repository.getTotalFlightByYear(year)
    }
}