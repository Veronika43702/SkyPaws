package ru.skypaws.domain.usecase.logbook

import ru.skypaws.domain.model.YearMonth
import ru.skypaws.domain.repository.LogbookRepository
import javax.inject.Inject

class GetYearsAndMonthsByCurrentUseCase @Inject constructor(
    private val repository: LogbookRepository
) {
    suspend operator fun invoke(year: Int, month: Int): List<YearMonth> {
        return repository.getYearsAndMonthsByCurrent(year, month)
    }
}