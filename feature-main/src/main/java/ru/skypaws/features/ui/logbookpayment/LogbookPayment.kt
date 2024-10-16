package ru.skypaws.features.ui.logbookpayment

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ru.skypaws.presentation.R
import ru.skypaws.features.model.PaidServicesState
import ru.skypaws.features.ui.commonUI.Payment
import ru.skypaws.features.utils.logbookDownload
import ru.skypaws.features.viewmodel.LogbookPayIntent
import ru.skypaws.features.viewmodel.LogbookPayViewModel
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogbookPayment(
    navigateTo: (route: String) -> Unit,
    logbookPayViewModel: LogbookPayViewModel = hiltViewModel(),
) {
    val logbookList = listOf(
        R.string.excel,
        R.string.easa,
        R.string.faa
    )

    LaunchedEffect(Unit) {
        logbookPayViewModel.handleIntent(LogbookPayIntent.DefaultState)
    }


    // state for pull to refresh indicator
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    // State from the ViewModel
    val state by logbookPayViewModel.payState.collectAsState()

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
            logbookPayViewModel.handleIntent(LogbookPayIntent.Refresh)
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
        Payment(
            content = { LogbookPaymentContent(list = logbookList) },
            price = state.logbookPrice,
            onButtonClick = { navigateTo(logbookDownload) },
        )
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
fun LogbookPaymentPreview(
    logbookList: List<Int> = listOf(
        R.string.excel,
        R.string.easa,
        R.string.faa
    ),
    state: PaidServicesState = PaidServicesState(
        logbookPrice = 1000
    )
) {
    SkyPawsTheme {
        Payment(
            content = { LogbookPaymentContent(list = logbookList) },
            price = state.logbookPrice,
            onButtonClick = { },
        )
    }
}