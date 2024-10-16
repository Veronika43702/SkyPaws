package ru.skypaws.presentation.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.skypaws.presentation.activity.MainActivity
import ru.skypaws.features.enterActivity.ui.enter.Enter
import ru.skypaws.features.enterActivity.ui.enterwithcode.EnterWithCode
import ru.skypaws.features.util.enterUI
import ru.skypaws.features.util.enterWithCodeUI
import ru.skypaws.features.enterActivity.viewmodel.EnterLoadingViewModel
import ru.skypaws.features.enterActivity.viewmodel.EnterViewModel

@Composable
fun EnterNavigation(
    enterWithCode: Boolean,
    enterLoadingViewModel: EnterLoadingViewModel = hiltViewModel(),
    enterViewModel: EnterViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = enterUI
    ) {
        composable(enterUI) {
            Enter(
                onEnter = {
                    navigateToMainActivity(context)
                },
                onEnterWithCode = { navController.navigate(enterWithCodeUI) },
                enterLoadingViewModel = enterLoadingViewModel,
                enterViewModel = enterViewModel
            )
        }


        composable(enterWithCodeUI) {
            EnterWithCode(
                onEnterWithCode = { navigateToMainActivity(context) },
                enterLoadingViewModel = enterLoadingViewModel,
                enterViewModel = enterViewModel
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
