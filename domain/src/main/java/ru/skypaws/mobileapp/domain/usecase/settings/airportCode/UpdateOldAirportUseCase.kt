package ru.skypaws.mobileapp.domain.usecase.settings.airportCode

import ru.skypaws.mobileapp.domain.repository.settings.AirportCodeSettingRepository
import javax.inject.Inject

class UpdateOldAirportUseCase @Inject constructor(
    private val repository: AirportCodeSettingRepository
) {
    suspend operator fun invoke(newCode: Int) {
        repository.updateOldAirportCodeWithNewValue(newCode)
    }
}