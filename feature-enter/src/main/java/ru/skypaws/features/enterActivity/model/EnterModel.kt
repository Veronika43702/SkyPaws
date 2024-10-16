package ru.skypaws.features.enterActivity.model

data class EnterModel (
    var id: String? = null,

    val loading: Boolean = false,

    val signedIn: Boolean = false,
    val codeIsSent: Boolean = false,
    val registered: Boolean = false,

    val codeSendingError: Boolean = false,
    val registerError: Boolean = false,
    val signInError: Boolean = false,

    val wrongData: Boolean = false,
    val userNotFound: Boolean = false,
    val aviabitServerTimeOut: Boolean = false,
    val networkError: Boolean = false,
)