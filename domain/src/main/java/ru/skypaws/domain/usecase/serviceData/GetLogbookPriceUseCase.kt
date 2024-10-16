package ru.skypaws.domain.usecase.serviceData

import ru.skypaws.domain.repository.ServiceDataRepository
import javax.inject.Inject

class GetLogbookPriceUseCase @Inject constructor(
    private val repository: ServiceDataRepository
) {
    operator fun invoke(): Int = repository.getLogbookPrice()
}