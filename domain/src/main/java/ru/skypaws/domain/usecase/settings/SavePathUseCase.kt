package ru.skypaws.domain.usecase.settings

import ru.skypaws.domain.repository.SettingsRepository
import javax.inject.Inject

class SavePathUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(uri: String) {
        repository.savePath(uri)
    }
}