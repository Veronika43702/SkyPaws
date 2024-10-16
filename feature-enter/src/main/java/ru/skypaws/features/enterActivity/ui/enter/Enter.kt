package ru.skypaws.features.enterActivity.ui.enter

import android.app.Activity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import ru.skypaws.features.enterActivity.model.EnterModel
import ru.skypaws.features.enterActivity.ui.common.SnackBarDuringError
import ru.skypaws.presentation.model.LoadingDataState
import ru.skypaws.presentation.viewmodel.UpdateViewModel

@Composable
fun Enter(
    onEnter: () -> Unit,
    onEnterWithCode: () -> Unit,
    loadAviabitAndServiceData: () -> Unit,
    signIn: (String, String) -> Unit,
    stopLoadingData: () -> Unit,
    loadCrewPlanAndLogbook: () -> Unit,
    loadCrewPlan: () -> Unit,
    loadLogbook: () -> Unit,
    enterDataState: EnterModel,
    loadingState: LoadingDataState,
    updateViewModel: UpdateViewModel = hiltViewModel()
) {
    // updateState: Update state (from update VM)
    val updateState by updateViewModel.updateState.collectAsState()

    // if new version apk downloaded -> install
    val context = LocalContext.current
    if (updateState.downloaded) {
        if (context is Activity) {
            updateViewModel.installApk(context)
        }
    }

    LaunchedEffect(Unit) {
        updateViewModel.checkUpdates()
    }

    LaunchedEffect(enterDataState.signedIn) {
        if (enterDataState.signedIn) {
            loadAviabitAndServiceData()
        }
    }

    LaunchedEffect(loadingState) {
        when {
            loadingState.loginAviabitPage -> onEnterWithCode()
            loadingState.crewPlanLoaded && loadingState.logbookLoaded -> {
                stopLoadingData()
                onEnter()
            }

            loadingState.error -> {
                stopLoadingData()
            }
        }
    }

    val username = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }


    // snackbar при ошибке загрузки данных из Aviabit (loadCrewPlan(), getlogbookDetailed())
    val snackBarHostState = remember { SnackbarHostState() }
    SnackBarDuringError(
        loadingState = loadingState,
        loadCrewPlanAndLogbook = loadCrewPlanAndLogbook,
        loadCrewPlan = loadCrewPlan,
        loadLogbook = loadLogbook,
        snackBarHostState = snackBarHostState,
    )

    EnterBox(
        loadingState = loadingState,
        updateState = updateState,
        enterDataState = enterDataState,
        username = username,
        password = password,
        passwordVisible = passwordVisible,
        downloadApk = { updateViewModel.downloadApk() },
        snackBarHostState = snackBarHostState,
        signIn = { signIn(username.value, password.value) },
        onEnterWithCode = onEnterWithCode
    )
}




