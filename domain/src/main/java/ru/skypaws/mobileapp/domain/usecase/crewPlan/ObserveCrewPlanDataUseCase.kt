package ru.skypaws.mobileapp.domain.usecase.crewPlan

import kotlinx.coroutines.flow.Flow
import ru.skypaws.mobileapp.domain.repository.CrewPlanRepository
import javax.inject.Inject

class ObserveCrewPlanDataUseCase @Inject constructor(
    private val repository: CrewPlanRepository
) {
    operator fun invoke(): Flow<List<ru.skypaws.mobileapp.domain.model.CrewPlanEvent>> {
        return repository.data
    }
}