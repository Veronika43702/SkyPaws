package ru.skypaws.domain.usecase.logbook

import ru.skypaws.domain.repository.LogbookRepository
import javax.inject.Inject

class GetLogbookItemDetailedUseCase @Inject constructor(
    private val repository: LogbookRepository
) {
    suspend operator fun invoke(year: Int, month: Int) {
        repository.getLogbookItemDetailed(year, month)
    }
}