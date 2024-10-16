package ru.skypaws.mobileapp.domain.usecase.settings.airportCode

import ru.skypaws.mobileapp.domain.repository.settings.AirportCodeSettingRepository
import javax.inject.Inject

class CheckForNewCodeUseCase @Inject constructor(
    private val repository: AirportCodeSettingRepository
) {
    suspend operator fun invoke(): Boolean = repository.isNewCodeSet()
}