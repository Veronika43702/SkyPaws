package ru.skypaws.data.error

class ApiErrors(val code: Int, message: String?) : Exception(message)
