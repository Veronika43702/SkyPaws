package ru.skypaws.mobileapp.domain.usecase.userPayInfo

import ru.skypaws.mobileapp.domain.repository.user.UserPayInfoRepository
import javax.inject.Inject

class GetLogbookExpDateUseCase @Inject constructor(
    private val repository: UserPayInfoRepository
){
    suspend operator fun invoke(): Long = repository.getLogbookExpDate()
}