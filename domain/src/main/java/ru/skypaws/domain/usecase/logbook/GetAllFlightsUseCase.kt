package ru.skypaws.domain.usecase.logbook

import ru.skypaws.domain.model.LogbookFlight
import ru.skypaws.domain.repository.LogbookRepository
import javax.inject.Inject

class GetAllFlightsUseCase @Inject constructor(
    private val repository: LogbookRepository
) {
    suspend operator fun invoke(): List<LogbookFlight> {
        return repository.getAllFlights()
    }
}