package ru.skypaws.mobileapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LogbookFlightEntity(
    val year: Int = 0,
    val month: Int = 0,
    @PrimaryKey
    val dateFlight: String = "",
    val dateLandingPlan: String = "",
    val flight: String = "",
    val airportTakeoff: String = "",
    val airportLanding: String = "",
    val airportTakeoffCode: String = "",
    val airportLandingCode: String = "",
    val plnType: String? = null,
    val pln: String? = null,
    val chair: String? = null,
    val chairCode: String? = null,
    val timeFlight: Int = 0,
    val timeBlock: Int = 0,
    val timeNight: Int = 0,
    val timeBiologicalNight: Int = 0,
    val timeWork: Int = 0,
    val type: Int = 0,
    val latTo: Double = 0.0,
    val longTo: Double = 0.0,
    val latLa: Double = 0.0,
    val longLa: Double = 0.0,
    val bufferTimeFlight: Int? = null,
    val independentFlight: Int? = null,
    val engineAfter: Int = 0,
    val engineBefore: Int = 0,
    val workAfter: Int = 0,
    val workBefore: Int = 0,
    val rest: Int = 0,
    val parking: Int = 0,
    val landings: Int = 0,
    val distance: Int = 0,
    val takeoff: Boolean = false,
    val landing: Boolean = false,
    val landingsOnDevices: Int = 0,
    val timeOnDevices: Int = 0,
    val splittedShift: Boolean = false,
    val pplsName: String? = null,
    val pplsCode: String? = null,
)
