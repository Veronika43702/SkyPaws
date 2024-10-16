package ru.skypaws.mobileapp.domain.model

data class CrewPlanEvent(
    val flight: String = "DP",
    val flightDate: String? = null,
    val dateTakeoff: String = "",
    val dateTakeoffReal: String? = null,
    val dateTakeoffCalculation: String? = null,
    val dateTakeoffAirParking: String? = null,
    val dateLandingAirParking: String? = null,
    val dateLanding: String? = null,
    val dateLandingReal: String? = null,
    val dateLandingCalculation: String? = null,
    val plnType: String? = null,
    val pln: String? = null,
    val maintenance: Int = 0,
    val airPortTOCode: String? = null,
    val airPortLACode: String? = null,
    val status: Int = 0,
    val crew: String? = null,
    val comment: String? = null,
)