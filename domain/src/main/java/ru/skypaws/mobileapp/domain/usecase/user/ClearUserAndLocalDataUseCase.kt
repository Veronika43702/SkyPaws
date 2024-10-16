package ru.skypaws.mobileapp.domain.usecase.user

import ru.skypaws.mobileapp.domain.repository.CrewPlanRepository
import ru.skypaws.mobileapp.domain.repository.logbook.LogbookAndFlightRepository
import ru.skypaws.mobileapp.domain.repository.user.UserRepository
import javax.inject.Inject

class ClearUserAndLocalDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val logbookAndFlightRepository: LogbookAndFlightRepository,
    private val crewPlanRepository: CrewPlanRepository
){
    suspend operator fun invoke() {
        userRepository.clearUserData()
        logbookAndFlightRepository.deleteLogbookAndFlights()
        crewPlanRepository.deleteCrewPlan()
    }
}