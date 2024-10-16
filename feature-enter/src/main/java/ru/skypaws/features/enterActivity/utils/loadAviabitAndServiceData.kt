package ru.skypaws.features.enterActivity.utils

inline fun loadAviabitAndServiceData(
    loadCrewPlan: () -> Unit,
    fetchLogbookAndLogbookFlightsFromServer: () -> Unit,

    getPayInfo: () -> Unit,
    getPriceInfo: () -> Unit,

    loadingData: () -> Unit,
) {
    // status loadingAviabitData to show info Card
    loadingData()

    // data for app
    getPayInfo()
    getPriceInfo()

    // Aviabit data
    loadCrewPlan()
    fetchLogbookAndLogbookFlightsFromServer()
}