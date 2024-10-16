package ru.skypaws.features.enterActivity.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ru.skypaws.presentation.R
import ru.skypaws.features.enterActivity.model.EnterModel
import ru.skypaws.presentation.model.UpdateModel
import ru.skypaws.presentation.model.LoadingDataState
import ru.skypaws.presentation.ui.CardForUpdate

@Composable
internal fun Boxes(
    updateState: UpdateModel = UpdateModel(),
    loadingState: LoadingDataState,
    enterDataState: EnterModel,
    snackBarHostState: SnackbarHostState,
    updateCard: Boolean = false,
    downloadApk: () -> Unit = {},
    content: @Composable (
        modifierImage: Modifier,
        modifierColumn: Modifier
    ) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight()
                    .weight(0.8f)
            )

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content(
                    Modifier.weight(0.8f),
                    Modifier.weight(1f),
                )
            }
        }

        LoadingStates(
            enterDataState,
            loadingState,
            snackBarHostState,
            Modifier.align(Alignment.BottomCenter)
        )

        if (updateCard) {
            if (updateState.newDB && !updateState.downloading) {
                CardForUpdate(update = { downloadApk() })
            }

            if (updateState.downloading) {
                ru.skypaws.presentation.ui.CardDownloadProgress(updateState)
            }
        }
    }
}