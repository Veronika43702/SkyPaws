package ru.skypaws.mobileapp.presentation.enter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.skypaws.features.utils.enterWithCodeUI

@AndroidEntryPoint
class EnterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val enterWithCode = intent.getBooleanExtra(enterWithCodeUI, false)

        setContent {
            ru.skypaws.presentation.ui.theme.SkyPawsTheme {
                EnterNavigation(enterWithCode = enterWithCode)
            }
        }
    }
}