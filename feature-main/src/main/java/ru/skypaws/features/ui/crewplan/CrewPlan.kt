package ru.skypaws.features.ui.crewplan

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ru.skypaws.presentation.R
import ru.skypaws.presentation.model.UpdateModel
import ru.skypaws.features.viewmodel.CrewPlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrewPlan(
    isLoadingNeeded: Boolean,
    newStateForLoadingNeeded: (Boolean) -> Unit,
    signOut: () -> Unit,

    userDataState: ru.skypaws.features.model.UserModel,
    getUserFromService: () -> Unit,
    getPayInfo: () -> Unit,
    setDefaultValueToUserDataState: () -> Unit,

    updateState: UpdateModel,
    installApk: () -> Unit,
    downloadApk: () -> Unit,
    checkUpdates: () -> Unit,
    setInitialUpdateState: () -> Unit,

    // viewModel for CrewPlan content and to load/update user data and Aviabit data
    crewPlanViewModel: CrewPlanViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        crewPlanViewModel.startObservingCrewPlanData()
    }
    /*
        crewPlanState: flights & refresh()
        loadingState: state for loading data (getLogbook, loadCrewPlan)
     */
    val crewPlanState by crewPlanViewModel.crewPlanState.collectAsState()
    val loadingState by crewPlanViewModel.loadingState.collectAsState()

    // if new version apk downloaded -> install apk
    if (updateState.downloaded && updateState.newDB) {
        installApk()
    }

    /*
        loading new data during app init, when no force update is needed
        (absence of new DB in new version)
        as system theme may be changed, and app recreates,
        checkingUpdate and loading only during first app init
    */
    LaunchedEffect(isLoadingNeeded) {
        if (isLoadingNeeded) {
            checkUpdates()
        }
    }

    // when checkUpdates() finished and no newDB -> load data
    if (
        isLoadingNeeded
        && updateState.checkUpdatesFinished
        && !updateState.newDB
    ) {
        setInitialUpdateState()
        getUserFromService()
        getPayInfo()
        crewPlanViewModel.getPriceInfo()
    }

    // after getting user data by getUser() -> load Aviabit data
    if (userDataState.userInfoLoaded) {
        setDefaultValueToUserDataState()
        newStateForLoadingNeeded(false)
        crewPlanViewModel.loadCrewPlan()
        // TODO !!! uncomment request for logbook
        //viewModel.getLogbookDetailedLastMonth()
        crewPlanViewModel.loadingAviabitData()
    }

    // user becomes not verified (aviabit cookie is invalid) -> navigate to enterWithCode UI
    if (userDataState.userNotVerified || loadingState.loginAviabitPage) {
        crewPlanViewModel.loadingDataStopped()
        signOut()
    }

    // state of completion of aviabit data loading
    if (loadingState.crewPlanLoaded) { // TODO uncomment! && state.logbookLoaded) {
        crewPlanViewModel.loadingDataStopped()
    }

    // after airport code is changed in Settings UI -> loading crewPlan data with new code
    if (crewPlanState.isNewCode) {
        crewPlanViewModel.refresh()
    }

    // state for pull to refresh indicator
    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing = remember { mutableStateOf(false) }

    // snackbar during error of user and aviabit data loading (refresh() and OnCreate functions)
    val snackbarHostState = remember { SnackbarHostState() }
    val text =
        if (userDataState.errorGetUser || loadingState.error || crewPlanState.refreshError) {
            when {
                userDataState.errorGetUser -> stringResource(id = R.string.serverNotWork)

                loadingState.aviabitServerTimeOut || crewPlanState.refreshErrorOfResponse ->
                    stringResource(id = R.string.aviabitNotWork)

                else -> stringResource(id = R.string.unableToLoadData)
            }
        } else {
            ""
        }

    val snackBarButtonName = stringResource(id = R.string.retry).uppercase()
    // state.error == true and error presence during refresh() -> showSnackbar
    LaunchedEffect(userDataState.errorGetUser || loadingState.error || crewPlanState.refreshError) {
        // disable aviabit loading card with linear progress
        if (loadingState.logbookError || loadingState.crewPlanError) {
            crewPlanViewModel.loadingDataStopped()
        }

        if (userDataState.errorGetUser || loadingState.error || crewPlanState.refreshError) {
            val result = snackbarHostState.showSnackbar(
                message = text,
                duration = SnackbarDuration.Long,
                withDismissAction = true,
                actionLabel = snackBarButtonName,
            )

            if (result == SnackbarResult.ActionPerformed) {
                when {
                    // error during getUser()
                    userDataState.errorGetUser -> {
                        getUserFromService()
                    }

                    // errors in both functions
                    loadingState.logbookError && loadingState.crewPlanError -> {
                        crewPlanViewModel.loadCrewPlan()
                        crewPlanViewModel.fetchLogbookDetailedLastMonth()
                        crewPlanViewModel.loadingAviabitData()
                    }

                    // error in loadCrewPlan()
                    loadingState.crewPlanError -> {
                        crewPlanViewModel.loadCrewPlan()
                        crewPlanViewModel.loadingAviabitData()
                    }

                    // error in getLogbookDetailedLastMonth()
                    loadingState.logbookError -> {
                        crewPlanViewModel.fetchLogbookDetailedLastMonth()
                        crewPlanViewModel.loadingAviabitData()
                    }

                    // error during refresh()
                    crewPlanState.refreshError -> {
                        crewPlanViewModel.refresh()
                    }
                }
            }
        }
    }

    CrewPlanMain(
        crewPlanState = crewPlanState,
        loadingState = loadingState,
        userDataState = userDataState,
        isRefreshing = isRefreshing,
        refreshCrewPlan = { crewPlanViewModel.refresh() },
        pullToRefreshState = pullToRefreshState,
        snackbarHostState = snackbarHostState,
        updateState = updateState,
        downloadApk = downloadApk,

        getTimeBetween = { fromDate, toDate -> crewPlanViewModel.getTimeBetween(fromDate, toDate) },
        getISOTimeLandingCalculated = { date, dateFrom, dateUntil ->
            crewPlanViewModel.getISOTimeLandingCalculated(
                date,
                dateFrom,
                dateUntil
            )
        },
        addMinutesToTime = { date, minutes ->
            crewPlanViewModel.addMinutesToTime(
                date,
                minutes
            )
        },
        getDateFromISO = { date -> crewPlanViewModel.getDateFromISO(date) },
        getTimeFromISO = { date -> crewPlanViewModel.getTimeFromISO(date) },
        getTimeLandingCalculated = { date, dateFrom, dateUntil ->
            crewPlanViewModel.getTimeLandingCalculated(
                date,
                dateFrom,
                dateUntil
            )
        }
    )
}
