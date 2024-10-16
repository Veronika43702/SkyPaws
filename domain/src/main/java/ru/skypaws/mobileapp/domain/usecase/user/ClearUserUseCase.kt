package ru.skypaws.mobileapp.domain.usecase.user

import ru.skypaws.mobileapp.domain.repository.user.UserRepository
import javax.inject.Inject

class ClearUserUseCase @Inject constructor(
    private val repository: UserRepository
){
    suspend operator fun invoke() {
        repository.clearUserData()
    }
}