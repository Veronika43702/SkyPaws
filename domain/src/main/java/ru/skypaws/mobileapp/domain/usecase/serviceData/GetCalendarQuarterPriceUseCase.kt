package ru.skypaws.mobileapp.domain.usecase.serviceData

import ru.skypaws.mobileapp.domain.repository.ServiceDataRepository
import javax.inject.Inject

class GetCalendarQuarterPriceUseCase @Inject constructor(
    private val repository: ServiceDataRepository
) {
    suspend operator fun invoke(): Int = repository.getCalendarQuarterPrice()
}