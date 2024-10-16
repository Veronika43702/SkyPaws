package ru.skypaws.mobileapp.domain.usecase.user

import ru.skypaws.mobileapp.domain.repository.user.UserRepository
import javax.inject.Inject

class CheckAuthorizationUseCase @Inject constructor(
    private val repository: UserRepository
){
    suspend operator fun invoke(): Boolean {
        return repository.isAuthorized()
    }
}