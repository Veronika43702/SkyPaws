package ru.skypaws.data.mapper

import ru.skypaws.data.source.entity.CrewPlanEventEntity
import ru.skypaws.domain.model.CrewPlanEvent

fun CrewPlanEventEntity.toDomain(): CrewPlanEvent {
    return CrewPlanEvent(
        flight = flight,
        flightDate = flightDate,
        dateTakeoff = dateTakeoff,
        dateTakeoffReal = dateTakeoffReal,
        dateTakeoffCalculation = dateTakeoffCalculation,
        dateTakeoffAirParking = dateTakeoffAirParking,
        dateLandingAirParking = dateLandingAirParking,
        dateLanding = dateLanding,
        dateLandingReal = dateLandingReal,
        dateLandingCalculation = dateLandingCalculation,
        plnType = plnType,
        pln = pln,
        maintenance = maintenance,
        airPortTOCode = airPortTOCode,
        airPortLACode = airPortLACode,
        status = status,
        crew = crew,
        comment = comment
    )
}

fun CrewPlanEvent.toEntity(): CrewPlanEventEntity {
    return CrewPlanEventEntity(
        flight = flight,
        flightDate = flightDate,
        dateTakeoff = dateTakeoff,
        dateTakeoffReal = dateTakeoffReal,
        dateTakeoffCalculation = dateTakeoffCalculation,
        dateTakeoffAirParking = dateTakeoffAirParking,
        dateLandingAirParking = dateLandingAirParking,
        dateLanding = dateLanding,
        dateLandingReal = dateLandingReal,
        dateLandingCalculation = dateLandingCalculation,
        plnType = plnType,
        pln = pln,
        maintenance = maintenance,
        airPortTOCode = airPortTOCode,
        airPortLACode = airPortLACode,
        status = status,
        crew = crew,
        comment = comment
    )
}