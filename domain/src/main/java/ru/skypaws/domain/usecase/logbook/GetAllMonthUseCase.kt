package ru.skypaws.domain.usecase.logbook

import ru.skypaws.domain.model.YearMonthType
import ru.skypaws.domain.repository.LogbookRepository
import javax.inject.Inject

class GetAllMonthUseCase @Inject constructor(
    private val repository: LogbookRepository
) {
    suspend operator fun invoke(): List<YearMonthType> {
        return repository.getAllMonth()
    }
}