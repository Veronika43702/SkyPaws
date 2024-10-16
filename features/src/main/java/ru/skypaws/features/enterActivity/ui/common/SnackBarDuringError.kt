package ru.skypaws.features.enterActivity.ui.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import ru.skypaws.features.R
import ru.skypaws.features.model.LoadingDataState

@Composable
internal fun SnackBarDuringError(
    loadingState: LoadingDataState,
    stopLoadingData: () -> Unit,
    loadCrewPlanAndLogbook: () -> Unit,
    loadCrewPlan: () -> Unit,
    loadLogbook: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    // snackbar при ошибке загрузки данных из Aviabit (loadCrewPlan(), getlogbookDetailed())
    val text = if (loadingState.error) {
        stopLoadingData()

        when {
            loadingState.aviabitServerTimeOut -> stringResource(
                id = R.string.aviabitNotWork
            )

            loadingState.errorToLoadData -> stringResource(
                id = R.string.unableToLoadData
            )

            else -> ""
        }
    } else {
        ""
    }

    // при изменении errodLoading state и наличии ошибки в loadCrewPlan() или getlogbookDetailed()
    LaunchedEffect(loadingState.error) {
        if (loadingState.error) {
            val result = snackBarHostState.showSnackbar(
                message = text,
                actionLabel = "Retry",
                duration = SnackbarDuration.Long
            )
            // действия при нажатии Retry на snackbar
            if (result == SnackbarResult.ActionPerformed) {
                when {
                    loadingState.logbookError && loadingState.crewPlanError -> loadCrewPlanAndLogbook()
                    loadingState.crewPlanError -> loadCrewPlan()
                    else -> loadLogbook()
                }
            }
        }
    }
}