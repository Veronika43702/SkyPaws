package ru.skypaws.mobileapp.domain.usecase.user

import ru.skypaws.mobileapp.domain.model.UserDomain
import ru.skypaws.mobileapp.domain.repository.user.UserRepository
import javax.inject.Inject

class GetLocalUserUseCase @Inject constructor(
    private val repository: UserRepository
){
    suspend operator fun invoke(): UserDomain {
        return repository.getLocalUser()
    }
}