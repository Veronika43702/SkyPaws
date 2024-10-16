package ru.skypaws.features.enterActivity.utils

import ru.skypaws.features.enterActivity.viewmodel.EnterLoadingViewModel
import ru.skypaws.features.enterActivity.viewmodel.EnterViewModel

fun loadAviabitAndServiceData(
    enterLoadingViewModel: EnterLoadingViewModel,
    enterViewModel: EnterViewModel
){
    // Aviabit data
    enterLoadingViewModel.loadCrewPlan()
    enterLoadingViewModel.getLogbookFlights()

    // data for app
    enterViewModel.getPayInfo()
    enterLoadingViewModel.getPriceInfo()

    // status loadingAviabitData to show info Card
    enterLoadingViewModel.loadingData()
}