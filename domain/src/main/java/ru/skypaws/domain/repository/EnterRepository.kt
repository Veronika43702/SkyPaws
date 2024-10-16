package ru.skypaws.domain.repository

interface EnterRepository  {
    suspend fun signIn(username: String, password: String)
    suspend fun getResponseForCode(username: String, password: String, airline: Int): String?
    suspend fun register(username: String, password: String, code: String, id: String)
}