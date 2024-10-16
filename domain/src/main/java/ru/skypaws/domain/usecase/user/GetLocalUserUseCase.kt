package ru.skypaws.domain.usecase.user

import ru.skypaws.domain.model.User
import ru.skypaws.domain.repository.UserRepository
import javax.inject.Inject

class GetLocalUserUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(): User {
        return repository.getLocalUser()
    }
}