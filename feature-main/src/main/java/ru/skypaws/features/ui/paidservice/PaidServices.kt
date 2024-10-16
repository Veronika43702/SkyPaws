package ru.skypaws.features.ui.paidservice

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.skypaws.presentation.R
import ru.skypaws.features.viewmodel.PaidServicesIntent
import ru.skypaws.features.viewmodel.PaidServicesViewModel
import ru.skypaws.presentation.ui.theme.SkyPawsTheme
import ru.skypaws.features.utils.calendar
import ru.skypaws.features.utils.logbookPay

data class PaidServices(
    val name: String,
    val isActive: Boolean = false,
    val date: String? = null,
    val priceDuration: List<PriceDuration> = emptyList(),
    val route: String,
)

data class PriceDuration(
    val price: Int = 0,
    val duration: Int = 0,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaidServices(
    navigateTo: (route: String) -> Unit,
    paidServicesViewModel: PaidServicesViewModel = hiltViewModel()
) {
    // Collect the state from the ViewModel
    val state by paidServicesViewModel.payState.collectAsState()

    LaunchedEffect(Unit) {
        paidServicesViewModel.handleIntent(PaidServicesIntent.DefaultState)
    }

    val listOfServices = listOf(
        PaidServices(
            name = stringResource(id = R.string.logbook),
            isActive = state.isLogbookPaid,
            date = state.logbookExpDate,
            priceDuration = listOf(
                PriceDuration(
                    state.logbookPrice,
                    1
                )
            ),
            route = logbookPay
        ),
        PaidServices(
            name = stringResource(id = R.string.calendar),
            isActive = state.isCalendarPaid,
            date = state.calendarExpDate,
            priceDuration = listOf(
                PriceDuration(
                    state.calendarMonthPrice,
                    1
                ),
                PriceDuration(
                    state.calendarQuarterPrice,
                    3
                ),
                PriceDuration(
                    state.calendarYearPrice,
                    12
                )
            ),
            route = calendar
        )
    )

    // state for pull to refresh indicator
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    // at the end of viewModel.checkPayment() -> state.refreshing = false ->
    LaunchedEffect(state.refreshing) {
        if (!state.refreshing) {
            // set pullToRefreshState.isRefreshing to false
            isRefreshing = false
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            paidServicesViewModel.handleIntent(PaidServicesIntent.Refresh)
        },
        state = pullToRefreshState,
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                state = pullToRefreshState
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start,
        ) {

            listOfServices.forEach { service ->
                Services(
                    service,
                    navigateTo
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp
            )
        }
    }
}

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
fun PaidServicesPreview(
    listOfServices: List<PaidServices> = listOf(
        PaidServices(
            name = stringResource(id = R.string.logbook),
            isActive = false,
            date = "29.09.2024",
            priceDuration = listOf(
                PriceDuration(
                    3000,
                    1
                )
            ),
            route = logbookPay
        ),
        PaidServices(
            name = stringResource(id = R.string.calendar),
            isActive = false,
            date = "10.08.2024",
            priceDuration = listOf(
                PriceDuration(
                    500,
                    1
                ),
                PriceDuration(
                    1000,
                    3
                ),
                PriceDuration(
                    3000,
                    12
                )
            ),
            route = calendar
        )
    )
) {
    SkyPawsTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start,
        ) {

            listOfServices.forEach { service ->
                ServicesPrev(
                    service
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp
            )
        }
    }
}

