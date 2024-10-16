package ru.skypaws.domain.usecase.crewPlan

import kotlinx.coroutines.flow.Flow
import ru.skypaws.domain.model.CrewPlanEvent
import ru.skypaws.domain.repository.CrewPlanRepository
import javax.inject.Inject

class ObserveCrewPlanDataUseCase @Inject constructor(
    private val repository: CrewPlanRepository
) {
    operator fun invoke(): Flow<List<CrewPlanEvent>> {
        return repository.data
    }
}