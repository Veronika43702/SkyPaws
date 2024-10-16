package ru.skypaws.mobileapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class CrewPlanEventDto(
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
    val dateCall: String? = null,
    //val canMakeCall: Int = 0, // sometimes Boolean, sometimes Int (0)
    val recordID: Int = 0,
    val pfRecordId: Int = 0,
    val changeTime: Boolean = false,
    val changePln: Boolean = false,
    val localAirport: String? = null,
    val safetyStatus: Int = 0,
    val isTaskReady: Int = 0,
    val recordType: Int = 0,
)

//data class Crew(
//    val flightCrew: FlightCrew = FlightCrew(),
//    val cabinCrew: CabinCrew = CabinCrew(),
//)
//
//data class FlightCrew(
//    val name: String = "",
//    val chair: String = "КВС",
//)
//
//data class CabinCrew(
//    val name: String = "",
//    val chair: String = "БП"
//)

//"crew":"<crew>
//<employee personnelId=\"1164\" armChair=\"КС\" crewType=\"0\" orderNumber=\"1\">Рыжков Д.В.</employee>
//<employee personnelId=\"1272\" armChair=\"СБЭ\" crewType=\"1\" orderNumber=\"1\">Горин Д.В.</employee>
//<employee personnelId=\"1858\" armChair=\"2П\" crewType=\"0\" orderNumber=\"2\">Симонович И.В.</employee>
//<employee personnelId=\"3785\" armChair=\"П\" crewType=\"4\" orderNumber=\"1\">Белов И.С.</employee>
//<employee personnelId=\"3793\" armChair=\"П\" crewType=\"4\" orderNumber=\"3\">Сиверин А.В.</employee>
//<employee personnelId=\"3903\" armChair=\"П\" crewType=\"4\" orderNumber=\"2\">Ионов Д.А.</employee>
//<employee personnelId=\"4016\" armChair=\"БП\" crewType=\"1\" orderNumber=\"4\">Юрьева Г.А.</employee>
//<employee personnelId=\"4987\" armChair=\"БП\" crewType=\"1\" orderNumber=\"2\">Карабаева М.В.</employee>
//<employee personnelId=\"4992\" armChair=\"БП\" crewType=\"1\" orderNumber=\"3\">Сельхов С.С.</employee>
//</crew>"
