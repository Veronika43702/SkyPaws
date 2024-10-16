package ru.skypaws.mobileapp.domain.usecase.crewPlan

import ru.skypaws.mobileapp.domain.repository.CrewPlanRepository
import javax.inject.Inject

class DeleteCrewPlanUseCase @Inject constructor(
    private val repository: CrewPlanRepository
) {
    suspend operator fun invoke() {
        repository.deleteCrewPlan()
    }
}