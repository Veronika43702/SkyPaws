package ru.skypaws.mobileapp.domain.usecase.logbook

import ru.skypaws.mobileapp.domain.model.MonthType
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookAndFlightRepository
import javax.inject.Inject

class GetMonthTypeListByCurrentYearUseCase @Inject constructor(
    private val repository: LogbookAndFlightRepository
) {
    suspend operator fun invoke(year: Int): List<MonthType> {
        return repository.getMonthTypeListByCurrentYear(year)
    }
}