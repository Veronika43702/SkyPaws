package ru.skypaws.domain.usecase.user

import ru.skypaws.domain.model.User
import ru.skypaws.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
){
    suspend operator fun invoke(): User {
        return repository.getUser()
    }
}