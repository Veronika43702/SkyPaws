package ru.skypaws.domain.usecase.settings

import ru.skypaws.domain.repository.SettingsRepository
import javax.inject.Inject

class GetPathUseCase @Inject constructor(
    private val repository: SettingsRepository
){
    operator fun invoke(): String? {
        return repository.getPath()
    }
}