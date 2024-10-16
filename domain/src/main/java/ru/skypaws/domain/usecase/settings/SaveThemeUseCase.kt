package ru.skypaws.domain.usecase.settings

import ru.skypaws.domain.repository.SettingsRepository
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(newTheme: Int) {
        repository.saveTheme(newTheme)
    }
}