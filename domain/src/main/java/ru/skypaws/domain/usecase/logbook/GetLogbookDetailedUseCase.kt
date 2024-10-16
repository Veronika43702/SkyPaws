package ru.skypaws.domain.usecase.logbook

import ru.skypaws.domain.model.YearMonth
import ru.skypaws.domain.repository.LogbookRepository
import javax.inject.Inject

class GetLogbookDetailedUseCase @Inject constructor(
    private val repository: LogbookRepository
) {
    suspend operator fun invoke(): List<YearMonth> {
        return repository.getLogbookDetailed()
    }
}