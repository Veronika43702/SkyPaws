package ru.skypaws.features.mainActivity.model

import ru.skypaws.domain.model.CrewPlanEvent

data class CrewPlanState(
    val flights: List<CrewPlanEvent> = emptyList(),

    val isNewCode: Boolean = false,

    val refreshFinished: Boolean = false,
    val refreshError: Boolean = false,
    val refreshErrorOfResponse: Boolean = false,
)