package ru.skypaws.presentation.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.skypaws.features.mainActivity.ui.crewplan.CrewPlan
import ru.skypaws.features.mainActivity.ui.logbook.Logbook
import ru.skypaws.features.mainActivity.ui.logbookdownload.LogbookDownLoad
import ru.skypaws.features.mainActivity.ui.logbookpayment.LogbookPayment
import ru.skypaws.features.mainActivity.ui.paidservice.PaidServices
import ru.skypaws.features.mainActivity.viewmodel.UserViewModel
import ru.skypaws.features.mainActivity.ui.settings.Settings
import ru.skypaws.features.util.crewPlanUI
import ru.skypaws.features.util.enterWithCodeUI
import ru.skypaws.features.util.logbook
import ru.skypaws.features.util.logbookDownload
import ru.skypaws.features.util.logbookPay
import ru.skypaws.features.util.payServices
import ru.skypaws.features.util.settings
import ru.skypaws.features.viewmodel.UpdateViewModel
import ru.skypaws.presentation.activity.EnterActivity


@Composable
fun MainNavigation(
    viewModel: UserViewModel,
    onThemeChange: (Int) -> Unit,
    initialIsLoadingNeeded: Boolean,
    initialDataLoaded: Boolean,
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

    val updateViewModel: UpdateViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = crewPlanUI

    ) {
        composable(crewPlanUI) {
            CrewPlan(
                isLoadingNeeded = isLoadingNeeded,
                newStateForLoadingNeeded = { isLoadingNeeded = it },
                navigateTo = { route -> navigateBetweenUi(navController, route) },
                signOut = { navigateToEnterActivity(context) },
                viewModel = hiltViewModel(),
                userViewModel = viewModel,
                updateViewModel = updateViewModel
            )
        }


        composable(logbook) {
            Logbook(
                navigateTo = { route -> navigateBetweenUi(navController, route) },
                userViewModel = viewModel,
                updateViewModel = updateViewModel
            )
        }

        composable(logbookPay) {
            LogbookPayment(
                navigateTo = { route -> navigateBetweenUi(navController, route) },
                viewModel = hiltViewModel(),
                userViewModel = viewModel,
                updateViewModel = updateViewModel
            )
        }

        composable(logbookDownload) {
            LogbookDownLoad(
                navigateTo = { route -> navigateBetweenUi(navController, route) },
                userViewModel = viewModel,
                updateViewModel = updateViewModel
            )
        }

        composable(payServices) {
            PaidServices(
                navigateTo = { route -> navigateBetweenUi(navController, route) },
                userViewModel = viewModel,
                updateViewModel = updateViewModel
            )
        }


        composable(settings) {
            Settings(
                navigateTo = { route -> navigateBetweenUi(navController, route) },
                onThemeChange = onThemeChange,
                userViewModel = viewModel,
                updateViewModel = updateViewModel
            )
        }


    }
}

fun navigateToEnterActivity(
    context: Context
) {
    val intent = Intent(context, EnterActivity::class.java)
    intent.putExtra(enterWithCodeUI, true)
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
        when (route) {
            crewPlanUI -> {
                // deletion of all UI layers including CrewPlan
                popUpTo(crewPlanUI) {
                    inclusive = true
                }
            }

            else -> {}
        }

    }
}