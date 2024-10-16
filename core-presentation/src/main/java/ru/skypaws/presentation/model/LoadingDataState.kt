package ru.skypaws.presentation.model

data class LoadingDataState(
    val loading: Boolean = false,
    val loadingAviabitData: Boolean= false,

    val logbookLoaded: Boolean = false,
    val crewPlanLoaded: Boolean = false,

    val error: Boolean = false,
    val aviabitServerTimeOut: Boolean = false,
    val errorToLoadData: Boolean = false,

    val loginAviabitPage: Boolean = false,

    val logbookError: Boolean = false,
    val crewPlanError: Boolean = false,
)