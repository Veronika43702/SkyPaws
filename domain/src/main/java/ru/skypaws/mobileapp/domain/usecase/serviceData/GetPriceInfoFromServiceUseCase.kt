package ru.skypaws.mobileapp.domain.usecase.serviceData

import ru.skypaws.mobileapp.domain.repository.ServiceDataRepository
import javax.inject.Inject

class GetPriceInfoFromServiceUseCase @Inject constructor(
    private val repository: ServiceDataRepository
) {
    suspend operator fun invoke() {
        repository.fetchPriceInfoFromService()
    }
}