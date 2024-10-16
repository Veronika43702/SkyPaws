package ru.skypaws.mobileapp.domain.usecase.logbook

import ru.skypaws.mobileapp.domain.model.FlightTime
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookAndFlightRepository
import javax.inject.Inject

class GetTotalTimeWithYearUseCase @Inject constructor(
    private val repository: LogbookAndFlightRepository
) {
    suspend operator fun invoke(year: Int): FlightTime {
        return repository.getTotalTimeWithYear(year)
    }
}