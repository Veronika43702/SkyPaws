package ru.skypaws.domain.usecase.crewPlan

import ru.skypaws.domain.repository.CrewPlanRepository
import javax.inject.Inject

class CheckForNewCodeUseCase @Inject constructor(
    private val repository: CrewPlanRepository
) {
    operator fun invoke(): Boolean {
        return repository.isNewCodeSet()
    }
}