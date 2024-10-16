package ru.skypaws.mobileapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CrewPlanEventEntity(
    val flight: String,
    val flightDate: String?,
    @PrimaryKey
    val dateTakeoff: String,
    val dateTakeoffReal: String?,
    val dateTakeoffCalculation: String?,
    val dateTakeoffAirParking: String?,
    val dateLandingAirParking: String?,
    val dateLanding: String?,
    val dateLandingReal: String?,
    val dateLandingCalculation: String?,
    val plnType: String?,
    val pln: String?,
    val maintenance: Int,
    val airPortTOCode: String?,
    val airPortLACode: String?,
    val status: Int,
    val crew: String?,
    val comment: String?,
)
//@Entity
//data class CrewEmb(
//    @Embedded
//    val flightCrew: FlightCrewEmb,
//    @Embedded
//    val cabinCrew: CabinCrewEmb,
//) {
//    fun toDto() = Crew(flightCrew.toDto(), cabinCrew.toDto())
//
//    companion object {
//        fun fromDto(dto: Crew): CrewEmb = with(dto) {
//            CrewEmb(FlightCrewEmb.fromDto(flightCrew), CabinCrewEmb.fromDto(cabinCrew))
//        }
//    }
//}
//
//data class FlightCrewEmb(
//    val nameFlight: String,
//    val chairFlight: String,
//) {
//    fun toDto() = FlightCrew(nameFlight, chairFlight)
//
//    companion object {
//        fun fromDto(dto: FlightCrew) = dto.let {
//            FlightCrewEmb(it.name, it.chair)
//        }
//    }
//}
//
//data class CabinCrewEmb(
//    val nameCabin: String,
//    val chairCabin: String
//) {
//    fun toDto() = CabinCrew(nameCabin, chairCabin)
//
//    companion object {
//        fun fromDto(dto: CabinCrew) = dto.let {
//            CabinCrewEmb(it.name, it.chair)
//        }
//    }
//}