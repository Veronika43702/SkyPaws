package ru.skypaws.mobileapp.domain.usecase.logbook

import ru.skypaws.mobileapp.domain.repository.logbook.LogbookAndFlightRepository
import javax.inject.Inject

class FetchAllOrUpdateLogbookAndFlightsFromServerUseCase @Inject constructor(
    private val repository: LogbookAndFlightRepository
) {
    suspend operator fun invoke() {
        repository.fetchAllOrUpdateLogbookAndFlightsFromServer()
    }
}