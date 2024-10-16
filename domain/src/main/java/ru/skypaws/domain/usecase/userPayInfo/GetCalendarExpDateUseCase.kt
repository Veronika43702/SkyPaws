package ru.skypaws.domain.usecase.userPayInfo

import ru.skypaws.domain.repository.UserPayInfoRepository
import javax.inject.Inject

class GetCalendarExpDateUseCase @Inject constructor(
    private val repository: UserPayInfoRepository
){
    operator fun invoke(): Long = repository.getLogbookExpDate()
}