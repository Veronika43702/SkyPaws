package ru.skypaws.domain.usecase.enter

import ru.skypaws.domain.repository.EnterRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: EnterRepository
){
    suspend operator fun invoke(
        username: String,
        password: String,
        code: String,
        id: String
    ) {
        repository.register(username, password, code, id)
    }
}