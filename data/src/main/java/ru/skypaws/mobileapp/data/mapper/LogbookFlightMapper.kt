package ru.skypaws.mobileapp.data.mapper

import ru.skypaws.mobileapp.data.model.dto.LogbookFlightDto
import ru.skypaws.mobileapp.data.model.entity.LogbookFlightEntity
import ru.skypaws.mobileapp.domain.model.LogbookFlight

fun LogbookFlightEntity.toDomain(): LogbookFlight {
    return LogbookFlight(
        year = year,
        month = month,
        dateFlight = dateFlight,
        dateLandingPlan = dateLandingPlan,
        flight = flight,
        airportTakeoff = airportTakeoff,
        airportLanding = airportLanding,
        airportTakeoffCode = airportTakeoffCode,
        airportLandingCode = airportLandingCode,
        plnType = plnType,
        pln = pln,
        chair = chair,
        chairCode = chairCode,
        timeFlight = timeFlight,
        timeBlock = timeBlock,
        timeNight = timeNight,
        timeBiologicalNight = timeBiologicalNight,
        timeWork = timeWork,
        type = type,
        latTo = latTo,
        longTo = longTo,
        latLa = latLa,
        longLa = longLa,
        bufferTimeFlight = bufferTimeFlight,
        independentFlight = independentFlight,
        engineAfter = engineAfter,
        engineBefore = engineBefore,
        workAfter = workAfter,
        workBefore = workBefore,
        rest = rest,
        parking = parking,
        landings = landings,
        distance = distance,
        takeoff = takeoff,
        landing = landing,
        landingsOnDevices = landingsOnDevices,
        timeOnDevices = timeOnDevices,
        splittedShift = splittedShift,
        pplsName = pplsName,
        pplsCode = pplsCode
    )
}

fun LogbookFlight.toEntity(): LogbookFlightEntity {
    return LogbookFlightEntity(
        year = year,
        month = month,
        dateFlight = dateFlight,
        dateLandingPlan = dateLandingPlan,
        flight = flight,
        airportTakeoff = airportTakeoff,
        airportLanding = airportLanding,
        airportTakeoffCode = airportTakeoffCode,
        airportLandingCode = airportLandingCode,
        plnType = plnType,
        pln = pln,
        chair = chair,
        chairCode = chairCode,
        timeFlight = timeFlight,
        timeBlock = timeBlock,
        timeNight = timeNight,
        timeBiologicalNight = timeBiologicalNight,
        timeWork = timeWork,
        type = type,
        latTo = latTo,
        longTo = longTo,
        latLa = latLa,
        longLa = longLa,
        bufferTimeFlight = bufferTimeFlight,
        independentFlight = independentFlight,
        engineAfter = engineAfter,
        engineBefore = engineBefore,
        workAfter = workAfter,
        workBefore = workBefore,
        rest = rest,
        parking = parking,
        landings = landings,
        distance = distance,
        takeoff = takeoff,
        landing = landing,
        landingsOnDevices = landingsOnDevices,
        timeOnDevices = timeOnDevices,
        splittedShift = splittedShift,
        pplsName = pplsName,
        pplsCode = pplsCode
    )
}

fun LogbookFlightDto.toDomain(year: Int, month: Int): LogbookFlight {
    return LogbookFlight(
        year = year,
        month = month,
        dateFlight = dateFlight,
        dateLandingPlan = dateLandingPlan,
        flight = flight,
        airportTakeoff = airportTakeoff,
        airportLanding = airportLanding,
        airportTakeoffCode = airportTakeoffCode,
        airportLandingCode = airportLandingCode,
        plnType = plnType,
        pln = pln,
        chair = chair,
        chairCode = chairCode,
        timeFlight = timeFlight,
        timeBlock = timeBlock,
        timeNight = timeNight,
        timeBiologicalNight = timeBiologicalNight,
        timeWork = timeWork,
        type = type,
        latTo = latTo,
        longTo = longTo,
        latLa = latLa,
        longLa = longLa,
        bufferTimeFlight = bufferTimeFlight,
        independentFlight = independentFlight,
        engineAfter = engineAfter,
        engineBefore = engineBefore,
        workAfter = workAfter,
        workBefore = workBefore,
        rest = rest,
        parking = parking,
        landings = landings,
        distance = distance,
        takeoff = takeoff,
        landing = landing,
        landingsOnDevices = landingsOnDevices,
        timeOnDevices = timeOnDevices,
        splittedShift = splittedShift,
        pplsName = pplsName,
        pplsCode = pplsCode
    )
}


fun List<LogbookFlight>.logbookFlightDomainToEntity(): List<LogbookFlightEntity> = map(
    LogbookFlight::toEntity
)

fun List<LogbookFlightEntity>.logbookFlightEntityToDomain(): List<LogbookFlight> = map(
    LogbookFlightEntity::toDomain
)