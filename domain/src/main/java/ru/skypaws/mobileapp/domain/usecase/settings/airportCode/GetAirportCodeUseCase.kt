package ru.skypaws.mobileapp.domain.usecase.settings.airportCode

import ru.skypaws.mobileapp.domain.repository.settings.AirportCodeSettingRepository
import javax.inject.Inject

class GetAirportCodeUseCase @Inject constructor(
    private val repository: AirportCodeSettingRepository
){
    suspend operator fun invoke(): Int = repository.getAirportCode()
}