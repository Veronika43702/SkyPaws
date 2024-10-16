package ru.skypaws.features.enterActivity.ui.enterwithcode

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import ru.skypaws.features.enterActivity.model.EnterModel
import ru.skypaws.features.enterActivity.ui.common.SnackBarDuringError
import ru.skypaws.presentation.model.LoadingDataState

@Composable
fun EnterWithCode(
    onEnterWithCode: () -> Unit,
    register: (String, String, String) -> Unit,
    getResponseForCode: (String, String) -> Unit,
    loadAviabitAndServiceData: () -> Unit,
    stopLoadingData: () -> Unit,
    loadCrewPlanAndLogbook: () -> Unit,
    loadCrewPlan: () -> Unit,
    loadLogbook: () -> Unit,
    enterDataState: EnterModel,
    loadingState: LoadingDataState,
) {
    LaunchedEffect(enterDataState.registered) {
        if (enterDataState.registered) {
            loadAviabitAndServiceData()
        }
    }

    LaunchedEffect(loadingState) {
        when {
            loadingState.crewPlanLoaded && loadingState.logbookLoaded -> {
                stopLoadingData()
                onEnterWithCode()
            }

            loadingState.error -> {
                stopLoadingData()
            }
        }
    }

    val username = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val code = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

    // snackbar при ошибке загрузки данных из Aviabit (loadCrewPlan(), getlogbookDetailed())
    val snackBarHostState = remember { SnackbarHostState() }
    SnackBarDuringError(
        loadingState = loadingState,
        loadCrewPlanAndLogbook = loadCrewPlanAndLogbook,
        loadCrewPlan = loadCrewPlan,
        loadLogbook = loadLogbook,
        snackBarHostState = snackBarHostState
    )

    EnterWithCodeBox(
        enterDataState = enterDataState,
        loadingState = loadingState,
        username = username,
        password = password,
        passwordVisible = passwordVisible,
        code = code,
        snackBarHostState = snackBarHostState,
        register = { register(username.value, password.value, code.value) },
        getResponseForCode = { getResponseForCode(username.value, password.value) }
    )
}