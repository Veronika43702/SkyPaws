package ru.skypaws.mobileapp.presentation.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.skypaws.mobileapp.domain.model.UserDomain
import ru.skypaws.features.ui.crewplan.CrewPlan
import ru.skypaws.features.ui.logbook.Logbook
import ru.skypaws.features.ui.logbookdownload.LogbookDownLoad
import ru.skypaws.features.ui.logbookpayment.LogbookPayment
import ru.skypaws.features.ui.paidservice.PaidServices
import ru.skypaws.features.ui.settings.Settings
import ru.skypaws.features.ui.topBarWithNavMenu.NavMenu
import ru.skypaws.features.utils.crewPlanUI
import ru.skypaws.features.utils.enterUI
import ru.skypaws.features.utils.enterWithCodeUI
import ru.skypaws.features.utils.logbook
import ru.skypaws.features.utils.logbookDownload
import ru.skypaws.features.utils.logbookPay
import ru.skypaws.features.utils.payServices
import ru.skypaws.features.utils.settings
import ru.skypaws.mobileapp.presentation.enter.EnterActivity
import ru.skypaws.presentation.R

@Composable
fun MainNavigation(
    user: UserDomain,
    userDataState: ru.skypaws.features.model.UserModel,
    clearUserAndAllLocalData: () -> Unit,
    getUserFromService: () -> Unit,
    getPayInfo: () -> Unit,
    setDefaultValueToUserDataState: () -> Unit,
    onThemeChange: (Int) -> Unit,
    initialIsLoadingNeeded: Boolean,
    initialDataLoaded: Boolean,
    saveTheme: (Int) -> Unit,
    saveAirportCode:(Int) -> Unit,
    savePath:(String) -> Unit,
    settingsState: ru.skypaws.features.model.SettingsModel
) {
    // As system theme may be changed and app recreates, loading new data is only during first init
    var isLoadingNeeded by rememberSaveable { mutableStateOf(initialIsLoadingNeeded) }
    var dataLoaded by rememberSaveable { mutableStateOf(initialDataLoaded) }

    val navController = rememberNavController()

    val context = LocalContext.current
    val window = (context as? Activity)?.window
    if (window != null) {
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        // system status bar must be always white as for dark theme (topbar background)
        insetsController.isAppearanceLightStatusBars = false
    }


    // if app OnCreate and dataLoaded = true (!false), then data in CrewPlan must be loaded
    LaunchedEffect(Unit) {
        if (!dataLoaded) {
            isLoadingNeeded = true
            dataLoaded = true
        }
    }

    val updateViewModel: ru.skypaws.presentation.viewmodel.UpdateViewModel = hiltViewModel()
    val updateState by updateViewModel.updateState.collectAsState()

    val installApk = {
        if (context is Activity) {
            updateViewModel.installApk(context)
        }
    }
    val downloadApk = { updateViewModel.downloadApk() }

    // if true -> drop down menu on top bar is needed
    val isActionNeed = remember { mutableStateOf(false) }
    val title = remember { mutableStateOf(crewPlanUI) }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            isActionNeed.value = when (destination.route) {
                logbook -> true
                else -> false
            }
            title.value = destination.route ?: ""
        }
    }

    val titleResource = when (title.value) {
        crewPlanUI -> stringResource(id = R.string.crew_plan)
        logbook, logbookPay, logbookDownload -> stringResource(id = R.string.logbook)
        payServices -> stringResource(id = R.string.paid_services)
        settings -> stringResource(id = R.string.settings)
        else -> ""
    }

    NavMenu(
        title = titleResource,
        isActionNeed = isActionNeed.value,
        navigateTo = { route ->
            if (route == enterUI) {
                clearUserAndAllLocalData()
                navigateToEnterActivity(context)
            } else
                navigateBetweenUi(navController, route)
        },
        user = user,
        updateState = updateState,
        installApk = installApk,
        downloadApk = { updateViewModel.downloadApk() },
    )
    { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = crewPlanUI,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(crewPlanUI) {
                CrewPlan(
                    isLoadingNeeded = isLoadingNeeded,
                    newStateForLoadingNeeded = { isLoadingNeeded = it },
                    signOut = {
                        clearUserAndAllLocalData()
                        navigateToEnterActivity(context, true)
                    },

                    userDataState = userDataState,
                    getUserFromService = getUserFromService,
                    getPayInfo = getPayInfo,
                    setDefaultValueToUserDataState = setDefaultValueToUserDataState,

                    checkUpdates = { updateViewModel.checkUpdates() },
                    setInitialUpdateState = { updateViewModel.setInitialUpdateState() },

                    updateState = updateState,
                    installApk = installApk,
                    downloadApk = downloadApk,
                )
            }


            composable(logbook) {
                Logbook()
            }

            composable(logbookPay) {
                LogbookPayment(
                    navigateTo = { route -> navigateBetweenUi(navController, route) }
                )
            }

            composable(logbookDownload) {
                LogbookDownLoad()
            }

            composable(payServices) {
                PaidServices(
                    navigateTo = { route -> navigateBetweenUi(navController, route) }
                )
            }

            composable(settings) {
                Settings(
                    onThemeChange = onThemeChange,
                    saveTheme = saveTheme,
                    saveAirportCode =saveAirportCode,
                    savePath = savePath,
                    settingsState = settingsState
                )
            }
        }
    }
}

fun navigateToEnterActivity(
    context: Context,
    navigateToEnterWithCode: Boolean = false
) {
    val intent = Intent(context, EnterActivity::class.java)
    intent.putExtra(enterWithCodeUI, navigateToEnterWithCode)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
    (context as Activity).finish()
}


// navigation between UI
fun navigateBetweenUi(
    navController: NavController,
    route: String
) {
    navController.navigate(route) {
        if (route == crewPlanUI) {
            // deletion of all UI layers including CrewPlan
            popUpTo(crewPlanUI) {
                inclusive = true
            }
        }
    }
}