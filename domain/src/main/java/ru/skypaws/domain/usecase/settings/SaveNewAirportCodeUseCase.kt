package ru.skypaws.domain.usecase.settings

import ru.skypaws.domain.repository.SettingsRepository
import javax.inject.Inject

class SaveNewAirportCodeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(newCode: Int) {
        repository.saveNewAirportCode(newCode)
    }
}