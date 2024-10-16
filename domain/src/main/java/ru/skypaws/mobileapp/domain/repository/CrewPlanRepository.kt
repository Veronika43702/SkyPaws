package ru.skypaws.mobileapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.skypaws.mobileapp.domain.model.CrewPlanEvent

interface CrewPlanRepository {
    /**
     * Gets Flow of [CrewPlanEvent] data from local storage.
     */
    val data: Flow<List<ru.skypaws.mobileapp.domain.model.CrewPlanEvent>>

    /**
     * Fetches [CrewPlanEvent] data from server and saves it.
     */
    suspend fun fetchCrewPlanFromServer()

    /**
     * Saves crew plan data locally
     */
    suspend fun saveCrewPlan(crewPlanEvents: List<ru.skypaws.mobileapp.domain.model.CrewPlanEvent>)

    /**
     * Gets crew plan data from local storage
     */
    suspend fun getCrewPlan(): List<ru.skypaws.mobileapp.domain.model.CrewPlanEvent>

    /**
     * Deletes CrewPlan events by dateTakeOff
     */
    suspend fun deleteEvent(dateTakeoff: String)

    /**
     * Clears CrewPlan Data
     */
    suspend fun deleteCrewPlan()
}

