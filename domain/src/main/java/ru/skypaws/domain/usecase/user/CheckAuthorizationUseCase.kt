package ru.skypaws.domain.usecase.user

import ru.skypaws.domain.repository.UserRepository
import javax.inject.Inject

class CheckAuthorizationUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(): Boolean {
        return repository.isAuthorized()
    }
}