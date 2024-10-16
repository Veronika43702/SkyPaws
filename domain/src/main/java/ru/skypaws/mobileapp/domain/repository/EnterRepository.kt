package ru.skypaws.mobileapp.domain.repository

interface EnterRepository  {
    /**
     * Gets user id as server response to sending data for code.
     */
    suspend fun getResponseForCode(username: String, password: String, airline: Int): String?

    /**
     * Registers with code and fetches [user][ru.skypaws.mobileapp.model.UserDomain] data to save
     * it locally.
     */
    suspend fun register(username: String, password: String, code: String, id: String)

    /**
     * Signs in and fetches [user][ru.skypaws.mobileapp.model.UserDomain] data to save it locally.
     */
    suspend fun signIn(username: String, password: String)
}