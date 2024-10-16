package ru.skypaws.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.skypaws.domain.model.CrewPlanEvent

interface CrewPlanRepository {
    val data: Flow<List<CrewPlanEvent>>
    fun isNewCodeSet(): Boolean
    suspend fun getCrewPlan()
    suspend fun delete()
}