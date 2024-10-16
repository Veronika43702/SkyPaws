package ru.skypaws.domain.usecase.enter

import ru.skypaws.domain.repository.EnterRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: EnterRepository
){
    suspend operator fun invoke(
        username: String,
        password: String
    ) {
        repository.signIn(username, password)
    }
}