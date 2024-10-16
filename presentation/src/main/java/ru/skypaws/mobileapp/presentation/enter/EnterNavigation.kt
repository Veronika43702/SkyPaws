package ru.skypaws.mobileapp.presentation.enter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.skypaws.features.enterActivity.ui.enter.Enter
import ru.skypaws.features.enterActivity.ui.enterwithcode.EnterWithCode
import ru.skypaws.features.enterActivity.utils.loadAviabitAndServiceData
import ru.skypaws.features.enterActivity.viewmodel.EnterLoadingViewModel
import ru.skypaws.features.enterActivity.viewmodel.EnterViewModel
import ru.skypaws.features.utils.enterUI
import ru.skypaws.features.utils.enterWithCodeUI
import ru.skypaws.mobileapp.presentation.main.MainActivity

@Composable
fun EnterNavigation(
    enterWithCode: Boolean,
    enterLoadingViewModel: EnterLoadingViewModel = hiltViewModel(),
    enterViewModel: EnterViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
) {
    val navController = rememberNavController()
    val loadAviabitAndServiceData = {
        loadAviabitAndServiceData(
            loadCrewPlan = { enterLoadingViewModel.loadCrewPlan() },
            fetchLogbookAndLogbookFlightsFromServer = { enterLoadingViewModel.fetchLogbookAndLogbookFlightsFromServer() },

            getPayInfo = { enterViewModel.getPayInfo() },
            getPriceInfo = { enterLoadingViewModel.getPriceInfo() },

            loadingData = { enterLoadingViewModel.loadingData() },
        )
    }

    /*
        loadingState: state for loading data (getLogbook, loadCrewPlan)
        enterDataState: enter states and user info
    */
    val loadingState by enterLoadingViewModel.loadingState.collectAsState()
    val enterDataState by enterViewModel.enterDataState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = enterUI
    ) {
        composable(enterUI) {
            Enter(
                onEnter = {
                    navigateToMainActivity(context)
                },
                onEnterWithCode = {
                    enterLoadingViewModel.resetState()
                    enterViewModel.setEnterDataStateToInitial()
                    navController.navigate(enterWithCodeUI)
                },
                loadAviabitAndServiceData = loadAviabitAndServiceData,
                signIn = { username, password -> enterViewModel.signIn(username, password) },
                stopLoadingData = { enterLoadingViewModel.loadingDataStopped() },
                loadCrewPlanAndLogbook = {
                    enterLoadingViewModel.loadCrewPlan()
                    enterLoadingViewModel.fetchLogbookAndLogbookFlightsFromServer()
                    enterLoadingViewModel.loadingData()
                },
                loadCrewPlan = {
                    enterLoadingViewModel.loadCrewPlan()
                    enterLoadingViewModel.loadingData()
                },
                loadLogbook = {
                    enterLoadingViewModel.fetchLogbookAndLogbookFlightsFromServer()
                    enterLoadingViewModel.loadingData()
                },
                enterDataState = enterDataState,
                loadingState = loadingState,
            )
        }


        composable(enterWithCodeUI) {
            EnterWithCode(
                onEnterWithCode = {
                    navigateToMainActivity(context)
                },
                loadAviabitAndServiceData = loadAviabitAndServiceData,
                register = { username, password, code ->
                    enterViewModel.register(
                        username,
                        password,
                        code
                    )
                },
                getResponseForCode = { username, password ->
                    enterViewModel.getResponseForCode(
                        username,
                        password,
                        1
                    )
                },
                stopLoadingData = { enterLoadingViewModel.loadingDataStopped() },
                loadCrewPlanAndLogbook = {
                    enterLoadingViewModel.loadCrewPlan()
                    enterLoadingViewModel.fetchLogbookAndLogbookFlightsFromServer()
                    enterLoadingViewModel.loadingData()
                },
                loadCrewPlan = {
                    enterLoadingViewModel.loadCrewPlan()
                    enterLoadingViewModel.loadingData()
                },
                loadLogbook = {
                    enterLoadingViewModel.fetchLogbookAndLogbookFlightsFromServer()
                    enterLoadingViewModel.loadingData()
                },
                enterDataState = enterDataState,
                loadingState = loadingState,
            )

            BackHandler {
                enterViewModel.setEnterDataStateToInitial()
                navController.popBackStack(enterUI, inclusive = false)
            }
        }
    }

    LaunchedEffect(enterWithCode) {
        if (enterWithCode) {
            navController.navigate(enterWithCodeUI)
        }
    }
}

fun navigateToMainActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    intent.putExtra("isLoadingNeeded", false)
    intent.putExtra("dataLoaded", true)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
    (context as Activity).finish()
}
