package ru.skypaws.mobileapp.domain.usecase.enter

import ru.skypaws.mobileapp.domain.repository.EnterRepository
import javax.inject.Inject

class GetResponseForCodeUseCase @Inject constructor(
    private val repository: EnterRepository
){
    suspend operator fun invoke(username: String, password: String, airline: Int): String? {
        return repository.getResponseForCode(username, password, airline)
    }
}