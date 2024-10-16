package ru.skypaws.domain.usecase.serviceData

import ru.skypaws.domain.repository.ServiceDataRepository
import javax.inject.Inject

class GetPriceInfoFromServiceUseCase @Inject constructor(
    private val repository: ServiceDataRepository
) {
    suspend operator fun invoke() {
        repository.getPriceInfoFromService()
    }
}