package ru.skypaws.features.ui.logbook

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.skypaws.mobileapp.domain.model.LogbookFlight
import ru.skypaws.mobileapp.domain.model.MonthType
import ru.skypaws.mobileapp.domain.model.YearMonth
import ru.skypaws.mobileapp.domain.model.YearMonthType
import ru.skypaws.mobileapp.domain.model.YearType
import ru.skypaws.presentation.R
import ru.skypaws.features.viewmodel.LogbookViewModel
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@Composable
fun Logbook(
    logbookViewModel: LogbookViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        logbookViewModel.loadInitialData()
    }
    val logbookState by logbookViewModel.logbookState.collectAsState()

    // snackbar with retry button in case of exception during logbookViewModel.loadInitialData().
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarText = stringResource(id = R.string.unableToLoadData)
    val snackBarButtonName = stringResource(id = R.string.retry).uppercase()
    LaunchedEffect(logbookState.error) {
        if (logbookState.error) {
            val result = snackbarHostState.showSnackbar(
                message = snackbarText,
                duration = SnackbarDuration.Indefinite,
                actionLabel = snackBarButtonName,
            )

            if (result == SnackbarResult.ActionPerformed) {
                logbookViewModel.loadInitialData()
            }
        }
    }

    val listState = rememberLazyListState()
    val showMonth = remember { mutableStateMapOf<Int, Boolean>() }
    val showFlight = remember { mutableStateMapOf<YearMonth, Boolean>() }
    var listIsReady by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Column(modifier = Modifier.fillMaxWidth())
        {
            LaunchedEffect(logbookState.listOfYear) {
                if (logbookState.listOfYear.isNotEmpty()) {
                    listIsReady = true
                }
            }
            if (listIsReady) {
                // Налет Total
                Total(logbookState)
                // список годов
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 1.dp)
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    itemsIndexed(logbookState.listOfYear) { index, year ->
                        LaunchedEffect(year.year) {
                            logbookViewModel.getTotalByYear(year.year)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(LocalCustomColors.current.logbookYear)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showMonth[index] = !(showMonth[index] ?: false)
                                    }
                            ) {
                                Year(
                                    year = year,
                                    state = logbookState,
                                    isClicked = showMonth[index] ?: false
                                )
                                if (showMonth[index] == true) {
                                    LaunchedEffect(year) {
                                        logbookViewModel.getTotalWithYear(year.year)
                                    }
                                    YearTotal(
                                        year,
                                        logbookState
                                    )
                                }
                            }

                            LaunchedEffect(year) {
                                logbookViewModel.getMonthList(year.year)
                            }
                            val monthList = logbookState.monthList[year.year] ?: emptyList()

                            if (monthList.isNotEmpty()) {
                                monthList.forEach { month ->
                                    val yearMonth = YearMonth(year.year, month.month)

                                    if (showMonth[index] == true) {
                                        Month(
                                            yearMonthType = YearMonthType(
                                                year.year,
                                                month.month,
                                                month.type
                                            ),
                                            state = logbookState,
                                            isClicked = showFlight[yearMonth] ?: false,
                                            onClick = {
                                                showFlight[yearMonth] =
                                                    !(showFlight[yearMonth] ?: false)
                                            }
                                        )

                                        if (showFlight[yearMonth] == true) {
                                            LaunchedEffect(month) {
                                                logbookViewModel.getFlightList(yearMonth)
                                            }
                                            val flightList =
                                                logbookState.flightList[yearMonth] ?: emptyList()
                                            if (flightList.isNotEmpty()) {
                                                flightList.forEach { flight ->
                                                    Flight(
                                                        flight = flight,
                                                        type = flight.type,
                                                        state = logbookState,
                                                        getDateNumberFromISO = { dateFlight ->
                                                            logbookViewModel.getDateNumberFromISO(
                                                                dateFlight
                                                            )
                                                        },
                                                        getDayOfWeek = { dateFlight ->
                                                            logbookViewModel.getDayOfWeek(
                                                                dateFlight
                                                            )
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    } else if (showMonth[index] == false) {
                                        showFlight[yearMonth] = false
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomCenter)
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
fun LogbookPreview(
    state: ru.skypaws.features.model.LogbookModel = ru.skypaws.features.model.LogbookModel(
        listOfYear = listOf(
            YearType(2024, 1),
            YearType(2023, 0),
        ),
        monthList = mapOf(
            2024 to listOf(
                MonthType(7, 0),
                MonthType(8, 1),
            ),
            2023 to listOf(
                MonthType(1, 0),
                MonthType(2, 0),
            )
        ),
        totalBlockWithYear = mapOf(2024 to "1000:10", 2023 to "500:10"),
        totalFlightWithYear = mapOf(2024 to "800:00", 2023 to "350:00"),
        totalNightWithYear = mapOf(2024 to "400:00", 2023 to "100:00"),
        totalBlockByYear = mapOf(2024 to "900:10", 2023 to "500:10"),
        totalFlightByYear = mapOf(2024 to "750:00", 2023 to "350:00"),
        totalNightByYear = mapOf(2024 to "300:00", 2023 to "100:00"),
        totalBlockByMonth = mapOf(
            YearMonthType(2024, 8, 3) to "20:00",
            YearMonthType(2024, 7, 0) to "70:00",
        ),
        totalFlightByMonth = mapOf(
            YearMonthType(2024, 8, 3) to "30:00",
            YearMonthType(2024, 7, 0) to "60:00",
        ),
        totalNightByMonth = mapOf(
            YearMonthType(2024, 8, 3) to "20:00",
            YearMonthType(2024, 7, 0) to "50:00",
        ),
        flightList = mapOf(
            YearMonth(2024, 8) to listOf(
                LogbookFlight(
                    flight = "123",
                    type = 3,
                    pln = "RA12345",
                    airportTakeoffCode = "LED",
                    airportLandingCode = "KGD"
                ),
                LogbookFlight(
                    flight = "456",
                    type = 3,
                    pln = "RA12345",
                    airportTakeoffCode = "LED",
                    airportLandingCode = "KGD"
                ),
            ),
            YearMonth(2024, 7) to listOf(
                LogbookFlight(
                    flight = "123",
                    pln = "RA12345",
                    airportTakeoffCode = "LED",
                    airportLandingCode = "KGD"
                ),
                LogbookFlight(
                    flight = "456",
                    pln = "RA12345",
                    airportTakeoffCode = "LED",
                    airportLandingCode = "KGD"
                ),
            ),
        ),
        flightBlock = mapOf(
            LogbookFlight(
                flight = "123",
                pln = "RA12345",
                airportTakeoffCode = "LED",
                airportLandingCode = "KGD"
            ) to "02:30",
            LogbookFlight(
                flight = "456",
                pln = "RA12345",
                airportTakeoffCode = "LED",
                airportLandingCode = "KGD"
            ) to "03:00"
        ),
        flightFlight = mapOf(
            LogbookFlight(
                flight = "123",
                pln = "RA12345",
                airportTakeoffCode = "LED",
                airportLandingCode = "KGD"
            ) to "01:30",
            LogbookFlight(
                flight = "456",
                pln = "RA12345",
                airportTakeoffCode = "LED",
                airportLandingCode = "KGD"
            ) to "02:00"
        ),
        flightNight = mapOf(
            LogbookFlight(
                flight = "123",
                pln = "RA12345",
                airportTakeoffCode = "LED",
                airportLandingCode = "KGD"
            ) to "00:30",
            LogbookFlight(
                flight = "456",
                pln = "RA12345",
                airportTakeoffCode = "LED",
                airportLandingCode = "KGD"
            ) to "01:00"
        ),

        ),
    listState: LazyListState = LazyListState(),
    showMonth: Boolean = false,
    listIsReady: Boolean = true,
) {
    SkyPawsTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            if (listIsReady) {
                // Налет Total
                TotalPreview()
                // список годов
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 1.dp)
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    itemsIndexed(state.listOfYear) { _, year ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(LocalCustomColors.current.logbookYear)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { }
                            ) {
                                YearPreview(
                                    year = year,
                                    state = state,
                                    isClicked = showMonth
                                )
                                if (showMonth) {
                                    YearTotalPreview(
                                        year = year,
                                        state = state,
                                    )
                                }
                            }

                            val monthList = state.monthList[year.year] ?: emptyList()

                            if (monthList.isNotEmpty()) {
                                monthList.forEach { month ->
                                    val yearMonth = YearMonth(year.year, month.month)

                                    if (showMonth) {
                                        MonthPreview(
                                            month = month,
                                            state = state,
                                        )

                                        val flightList =
                                            state.flightList[yearMonth] ?: emptyList()
                                        if (flightList.isNotEmpty()) {
                                            flightList.forEach { flight ->
                                                FlightPreview(
                                                    flight = flight,
                                                    type = flight.type,
                                                    state = state,
                                                )
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


