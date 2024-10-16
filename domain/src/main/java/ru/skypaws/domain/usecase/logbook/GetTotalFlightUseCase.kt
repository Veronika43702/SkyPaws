package ru.skypaws.domain.usecase.logbook

import ru.skypaws.domain.model.FlightTime
import ru.skypaws.domain.repository.LogbookRepository
import javax.inject.Inject

class GetTotalFlightUseCase @Inject constructor(
    private val repository: LogbookRepository
) {
    suspend operator fun invoke(): FlightTime {
        return repository.getTotalFlight()
    }
}