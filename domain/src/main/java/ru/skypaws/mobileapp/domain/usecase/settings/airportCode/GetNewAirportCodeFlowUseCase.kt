package ru.skypaws.mobileapp.domain.usecase.settings.airportCode

import kotlinx.coroutines.flow.Flow
import ru.skypaws.mobileapp.domain.repository.settings.AirportCodeSettingRepository
import javax.inject.Inject

class GetNewAirportCodeFlowUseCase @Inject constructor(
    private val repository: AirportCodeSettingRepository
){
    operator fun invoke(): Flow<Int> = repository.airportCode

}