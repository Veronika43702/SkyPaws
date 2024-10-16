package ru.skypaws.features.enterActivity.ui.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import ru.skypaws.presentation.R
import ru.skypaws.presentation.model.LoadingDataState

@Composable
internal fun SnackBarDuringError(
    loadingState: LoadingDataState,
    loadCrewPlanAndLogbook: () -> Unit,
    loadCrewPlan: () -> Unit,
    loadLogbook: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    // snackbar при ошибке загрузки данных из Aviabit (loadCrewPlan(), getlogbookDetailed())
    val text = when {
        loadingState.aviabitServerTimeOut -> stringResource(
            id = R.string.aviabitNotWork
        )

        loadingState.errorToLoadData -> stringResource(
            id = R.string.unableToLoadData
        )

        else -> ""
    }

    // при изменении errodLoading state и наличии ошибки в loadCrewPlan() или getlogbook()
    LaunchedEffect(loadingState.error, loadingState.logbookError, loadingState.crewPlanError) {
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
                    loadingState.logbookError -> loadLogbook()
                }
            }
        }
    }
}