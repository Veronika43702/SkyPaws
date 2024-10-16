package ru.skypaws.mobileapp.domain.usecase.settings.theme

import ru.skypaws.mobileapp.domain.repository.settings.ThemeSettingRepository
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(
    private val repository: ThemeSettingRepository
) {
    suspend operator fun invoke(newTheme: Int) {
        repository.saveTheme(newTheme)
    }
}