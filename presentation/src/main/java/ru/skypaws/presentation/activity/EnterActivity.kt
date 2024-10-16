package ru.skypaws.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.skypaws.features.ui.theme.SkyPawsTheme
import ru.skypaws.features.util.enterWithCodeUI
import ru.skypaws.presentation.navigation.EnterNavigation

@AndroidEntryPoint
class EnterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val enterWithCode = intent.getBooleanExtra(enterWithCodeUI, false)

        setContent {
            SkyPawsTheme {
                EnterNavigation(enterWithCode = enterWithCode)
            }
        }
    }
}