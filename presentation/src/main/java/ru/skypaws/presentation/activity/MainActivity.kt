package ru.skypaws.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.skypaws.features.ui.theme.SkyPawsTheme
import ru.skypaws.features.mainActivity.viewmodel.UserViewModel
import ru.skypaws.presentation.navigation.MainNavigation


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
    viewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    if (!viewModel.isAuthorized()) {
        val intent = Intent(context, EnterActivity::class.java)
        context.startActivity(intent)
        (context as Activity).finish()
    } else {
        var themeIsBeingChosen by remember { mutableIntStateOf(-1) }

        SkyPawsTheme(
            darkTheme = if (themeIsBeingChosen == -1) {
                when (viewModel.getTheme()) {
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
                viewModel = viewModel,
                onThemeChange = { newTheme ->
                    themeIsBeingChosen = newTheme
                },
                initialIsLoadingNeeded = initialIsLoadingNeeded,
                initialDataLoaded = initialDataLoaded,
            )
        }
    }
}