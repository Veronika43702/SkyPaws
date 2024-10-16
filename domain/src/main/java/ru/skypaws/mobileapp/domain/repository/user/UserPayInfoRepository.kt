package ru.skypaws.mobileapp.domain.repository.user

interface UserPayInfoRepository  {
    /**
     * Fetches [PayInfo][ru.skypaws.mobileapp.model.dto.PayInfoDto] from server
     * and saves it locally.
     */
    suspend fun fetchPayInfoAndSave()

    /**
     * Gets Logbook Expiration Date from local storage.
     * @return [Long]
     */
    suspend fun getLogbookExpDate(): Long

    /**
     * Gets Calendar Expiration Date from local storage.
     * @return [Long]
     */
    suspend fun getCalendarExpDate(): Long
}