package ru.skypaws.mobileapp.domain.repository.settings

import kotlinx.coroutines.flow.Flow

interface AirportCodeSettingRepository {
    /**
     * Gets airport code flow from local storage.
     * @return Flow<[Int]>
     */
    val airportCode: Flow<Int>

    /**
     * Saves [newCode] in local storage.
     */
    suspend fun saveNewAirportCode(newCode: Int)

    /**
     * Gets airport code from local storage.
     * @return [Int]
     */
    suspend fun getAirportCode(): Int

    /**
     * Updates old (previous) airport code with [newCode] in local storage.
     */
    suspend fun updateOldAirportCodeWithNewValue(newCode: Int)

    /**
     * Checks whether new airport code is set in settings (saved locally)
     * @return [Boolean]
     */
    suspend fun isNewCodeSet(): Boolean
}
