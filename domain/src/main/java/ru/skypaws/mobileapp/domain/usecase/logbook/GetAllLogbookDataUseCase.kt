package ru.skypaws.mobileapp.domain.usecase.logbook

import ru.skypaws.mobileapp.domain.model.LogbookModelData
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookAndFlightRepository
import javax.inject.Inject

class GetAllLogbookDataUseCase @Inject constructor(
    private val repository: LogbookAndFlightRepository
) {
    suspend operator fun invoke(): LogbookModelData {
        return repository.getAllLogbookData()
    }
}