package ru.skypaws.features.enterActivity.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.skypaws.presentation.R
import ru.skypaws.features.enterActivity.model.EnterModel
import ru.skypaws.presentation.model.LoadingDataState
import ru.skypaws.presentation.ui.theme.LocalCustomColors

@Composable
internal fun LoadingStates(
    enterDataState: EnterModel,
    loadingState: LoadingDataState,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier

){
    if (enterDataState.loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalCustomColors.current.shadow),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (loadingState.loadingAviabitData) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalCustomColors.current.shadow),
            contentAlignment = Alignment.Center
        ) {
            Card {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.aviabit_loading))
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator()
                }
            }

        }
    }

    // snackbar поверх всего окна
    SnackbarHost(
        hostState = snackBarHostState,
        modifier = modifier
    )
}