package ru.skypaws.domain.usecase.crewPlan

import ru.skypaws.domain.repository.CrewPlanRepository
import javax.inject.Inject

class GetCrewPlanUseCase @Inject constructor(
    private val repository: CrewPlanRepository
) {
    suspend operator fun invoke() {
        repository.getCrewPlan()
    }
}