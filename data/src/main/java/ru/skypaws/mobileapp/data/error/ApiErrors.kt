package ru.skypaws.mobileapp.data.error

class ApiErrors(val code: Int, message: String?) : Exception(message)
