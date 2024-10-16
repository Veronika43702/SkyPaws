package ru.skypaws.domain.usecase.logbook

import ru.skypaws.domain.model.YearType
import ru.skypaws.domain.repository.LogbookRepository
import javax.inject.Inject

class GetYearListUseCase @Inject constructor(
    private val repository: LogbookRepository
) {
    suspend operator fun invoke(): List<YearType> {
        return repository.getYearList()
    }
}