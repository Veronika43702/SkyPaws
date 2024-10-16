package ru.skypaws.features.ui.crewplan

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.skypaws.presentation.R
import ru.skypaws.features.model.CrewPlanState
import ru.skypaws.presentation.model.UpdateModel
import ru.skypaws.features.model.UserModel
import ru.skypaws.presentation.model.LoadingDataState
import ru.skypaws.presentation.ui.CardForUpdate
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme
import ru.skypaws.presentation.ui.CardDownloadProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrewPlanMain(
    crewPlanState: CrewPlanState,
    loadingState: LoadingDataState,
    userDataState: UserModel,
    isRefreshing: MutableState<Boolean>,
    refreshCrewPlan: () -> Unit,
    pullToRefreshState: PullToRefreshState,
    snackbarHostState: SnackbarHostState,
    updateState: UpdateModel,
    downloadApk: () -> Unit,

    getDateFromISO: (String?) -> String,
    getTimeFromISO: (String?) -> String,
    getTimeBetween: (String?, String?) -> String,
    getISOTimeLandingCalculated: (String?, String?, String?) -> String,
    addMinutesToTime: (String?, Long) -> String,
    getTimeLandingCalculated: (String?, String?, String?) -> String,
) {

    // there's no new version with new dataBase
    if (!updateState.newDB) {
        // at the end of viewModel.refresh() -> state.refreshFinished = true ->
        LaunchedEffect(crewPlanState.refreshFinished) {
            if (crewPlanState.refreshFinished) {
                // set pullToRefreshState.isRefreshing to false
                isRefreshing.value = false
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = {
            if (!updateState.newDB) {
                isRefreshing.value = true
                refreshCrewPlan()
            }
        },
        state = pullToRefreshState,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing.value,
                state = pullToRefreshState
            )
        }
    ) {
        // list of events/flights
        FlightLazyColumn(
            list = crewPlanState.flights,
            isClickableScrollable = !updateState.newDB,
            getDateFromISO = getDateFromISO,
            getTimeFromISO = getTimeFromISO,
            getTimeBetween = getTimeBetween,
            getTimeLandingCalculated = getTimeLandingCalculated,
            getISOTimeLandingCalculated = getISOTimeLandingCalculated,
            addMinutesToTime = addMinutesToTime
        )

        // SnackBar with errors
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomCenter)
        )
    }

    // new Version with new DB exists, update is necessary, app is blocked for use
    if (updateState.newDB && !updateState.downloading) {
        CardForUpdate(update = { downloadApk() })
    }

    // downloading apk for update
    if (updateState.downloading) {
        CardDownloadProgress(updateState)
    }

    // loading user data
    if (updateState.loading || userDataState.loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalCustomColors.current.shadow),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    // loading aviabit data
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "ru",
    name = "DefaultPreviewDark"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "ru",
    name = "DefaultPreviewLight"
)
@Composable
fun CrewPlanNavMenuPreview(
    crewPlanState: CrewPlanState = CrewPlanState(),
    userDataState: UserModel = UserModel(
        loading = false
    ),
    loadingState: LoadingDataState = LoadingDataState(),
    newDB: Boolean = false,
    refreshFinished: Boolean = false,
    isRefreshing: Boolean = false,
    downloading: Boolean = false,
    pullToRefreshState: PullToRefreshState = PullToRefreshState(),
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    updateState: UpdateModel = UpdateModel(
        newDB = false,
        downloading = false,
        downloaded = false
    )
) {
    SkyPawsTheme {
        // there's no new version with new dataBase
        if (!updateState.newDB) {
            // at the end of viewModel.refresh() -> state.refreshFinished = true ->
            LaunchedEffect(refreshFinished) {

            }
        }

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {},
            state = pullToRefreshState,
            modifier = Modifier,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    state = pullToRefreshState
                )
            }
        ) {
            // list of events/flights
            FlightLazyColumnPreview()

            // SnackBar with errors
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomCenter)
            )
        }

        // new Version with new DB exists, update is necessary, app is blocked for use
        if (updateState.newDB && !updateState.downloading) {
            CardForUpdate(update = { })
        }

        // downloading apk for update
        if (updateState.downloading) {
            CardDownloadProgress(updateState)
        }

        // loading user data
        if (updateState.loading || userDataState.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LocalCustomColors.current.shadow),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // loading aviabit data
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
    }
}
