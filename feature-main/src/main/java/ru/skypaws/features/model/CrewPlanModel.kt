package ru.skypaws.features.model

import ru.skypaws.mobileapp.domain.model.CrewPlanEvent

data class CrewPlanState(
    val flights: List<CrewPlanEvent> = emptyList(),

    val isNewCode: Boolean = false,

    val refreshFinished: Boolean = false,
    val refreshError: Boolean = false,
    val refreshErrorOfResponse: Boolean = false,
)