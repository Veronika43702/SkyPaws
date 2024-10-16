package ru.skypaws.mobileapp.data.datasource.local.settings

interface AirportCodeSettingLocalDataSource {
    suspend fun updateAirportCodeWithNewValue(newCode: Int)
    suspend fun isNewCodeSet(): Boolean
}