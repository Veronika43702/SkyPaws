package ru.skypaws.mobileapp.data.mapper

import ru.skypaws.mobileapp.data.model.dto.CrewPlanEventDto
import ru.skypaws.mobileapp.data.model.entity.CrewPlanEventEntity
import ru.skypaws.mobileapp.domain.model.CrewPlanEvent

fun CrewPlanEventEntity.toDomain(): ru.skypaws.mobileapp.domain.model.CrewPlanEvent {
    return ru.skypaws.mobileapp.domain.model.CrewPlanEvent(
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


fun CrewPlanEventDto.toDomain(): ru.skypaws.mobileapp.domain.model.CrewPlanEvent {
    return ru.skypaws.mobileapp.domain.model.CrewPlanEvent(
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

fun ru.skypaws.mobileapp.domain.model.CrewPlanEvent.toEntity(): CrewPlanEventEntity {
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

fun List<ru.skypaws.mobileapp.domain.model.CrewPlanEvent>.crewPlanEventDomainToEntity(): List<CrewPlanEventEntity> = map(
    ru.skypaws.mobileapp.domain.model.CrewPlanEvent::toEntity
)

fun List<CrewPlanEventDto>.crewPlanEventDtoToDomain(): List<ru.skypaws.mobileapp.domain.model.CrewPlanEvent> = map(
    CrewPlanEventDto::toDomain
)