package ru.skypaws.mobileapp.presentation.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.skypaws.features.viewmodel.SettingsViewModel
import ru.skypaws.features.viewmodel.UserViewModel
import ru.skypaws.presentation.ui.theme.SkyPawsTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val isLoadingNeeded = intent.getBooleanExtra("isLoadingNeeded", false)
        val dataLoaded = intent.getBooleanExtra("dataLoaded", false)

        setContent {
            MainScreen(isLoadingNeeded, dataLoaded)
        }
    }
}

@Composable
fun MainScreen(
    initialIsLoadingNeeded: Boolean,
    initialDataLoaded: Boolean,
    userViewModel: UserViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    LaunchedEffect(Unit) {
        println("app started")
        userViewModel.isAuthorized()
    }

    val settingState by settingsViewModel.settingState.collectAsState()
    val userState by userViewModel.userState.collectAsState()
    val userDataState by userViewModel.userDataState.collectAsState()

    if (userState.id != null) {
        settingsViewModel.observeSettings()
        var themeIsBeingChosen by remember { mutableIntStateOf(-1) }

        SkyPawsTheme(
            darkTheme = if (themeIsBeingChosen == -1) {
                when (settingState.theme) {
                    1 -> false
                    2 -> true
                    else -> isSystemInDarkTheme()
                }
            } else when (themeIsBeingChosen) {
                1 -> false
                2 -> true
                else -> isSystemInDarkTheme()
            }
        ) {
            MainNavigation(
                user = userState,
                userDataState = userDataState,
                clearUserAndAllLocalData = { userViewModel.clearUserAndAllLocalData() },
                getUserFromService = { userViewModel.getUserFromService() },
                getPayInfo = { userViewModel.getPayInfo() },
                setDefaultValueToUserDataState = { userViewModel.setDefaultValueToUserDataState() },
                onThemeChange = { newTheme ->
                    themeIsBeingChosen = newTheme
                },
                initialIsLoadingNeeded = initialIsLoadingNeeded,
                initialDataLoaded = initialDataLoaded,
                saveTheme = { newTheme -> settingsViewModel.saveTheme(newTheme) },
                saveAirportCode = { newCode -> settingsViewModel.saveAirportCode(newCode) },
                savePath = { newPath -> settingsViewModel.savePath(newPath) },
                settingsState = settingState
            )
        }
    } else if (!userDataState.userInfoLoaded) {
        navigateToEnterActivity(context)
    }
}