package ru.skypaws.domain.usecase.userPayInfo

import ru.skypaws.domain.repository.UserPayInfoRepository
import javax.inject.Inject

class GetPayInfoUseCase @Inject constructor(
    private val repository: UserPayInfoRepository
) {
    suspend operator fun invoke() {
        repository.getPayInfo()
    }
}