package ru.skypaws.features.ui.crewplan

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.skypaws.mobileapp.domain.model.CrewPlanEvent
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

// main content of CrewPlan UI: list of event/flights
@Composable
fun FlightLazyColumn(
    list: List<CrewPlanEvent>,
    isClickableScrollable: Boolean,
    getDateFromISO: (String?) -> String,
    getTimeFromISO: (String?) -> String,
    getTimeBetween: (String?, String?) -> String,
    getISOTimeLandingCalculated: (String?, String?, String?) -> String,
    addMinutesToTime: (String?, Long) -> String,
    getTimeLandingCalculated: (String?, String?, String?) -> String,
) {
    val listState = rememberLazyListState()
    // expanding item (additional information)
    val showNotConstantPart = remember { mutableStateMapOf<Int, Boolean>() }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        userScrollEnabled = isClickableScrollable

    ) {
        if (list.isNotEmpty()) {
            itemsIndexed(list) { index, item ->
                Column {
                    // event = flight
                    if (item.flight.startsWith("DP")) {
                        Flight(
                            event = item,
                            isClickable = isClickableScrollable,
                            onClick = {
                                showNotConstantPart[index] = !(showNotConstantPart[index] ?: false)
                            },
                            getDateFromISO = getDateFromISO,
                            getTimeFromISO = getTimeFromISO
                        )

                        // flight is clicked -> additional information is shown
                        if (showNotConstantPart[index] == true) {
                            // flight is in the future
                            if (item.status == 0 && item.dateTakeoffAirParking == null) {
                                ExpandedDataNotFlown(
                                    event = item,
                                    getTimeFromISO = getTimeFromISO,
                                    getTimeBetween = getTimeBetween,
                                )
                            } else {
                                // flight in progress or there's CFP
                                // (flight plan and calculated information)
                                ExpandedDataFlown(
                                    event = item,
                                    getTimeFromISO = getTimeFromISO,
                                    getTimeBetween = getTimeBetween,
                                    getTimeLandingCalculated = getTimeLandingCalculated,
                                    getISOTimeLandingCalculated = getISOTimeLandingCalculated,
                                    addMinutesToTime = addMinutesToTime
                                )
                            }
                        }
                        // event != flight (vacation, visit and etc.)
                    } else {
                        Event(
                            event = item,
                            isClickable = isClickableScrollable,
                            onClick = {
                                showNotConstantPart[index] = !(showNotConstantPart[index] ?: false)
                            },
                            getDateFromISO = getDateFromISO,
                            getTimeFromISO = getTimeFromISO
                        )
                    }

                    // event or flight is clicked -> comments are shown
                    if (showNotConstantPart[index] == true) {
                        if (!item.comment.isNullOrEmpty()) {
                            Comment(event = item)
                        }
                    }
                }
            }
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
fun FlightLazyColumnPreview(
    list: List<CrewPlanEvent> = listOf(
        CrewPlanEvent(
            flight = "DP-123",
            status = 2,
            dateTakeoffAirParking = "16:10",
            dateTakeoff = "10.08.24",
            dateTakeoffCalculation = "16:20",
            dateLanding = "18:00",
            dateLandingAirParking = "18:20",
            dateLandingCalculation = "18:00",
            airPortTOCode = "LED",
            airPortLACode = "KGD",
            pln = "RA12345"
        ),
        CrewPlanEvent(
            flight = "DP-456",
            status = 1,
            dateTakeoffAirParking = null,
            dateTakeoff = "10.08.24",
            dateTakeoffCalculation = "16:20",
            dateLanding = "18:00",
            dateLandingAirParking = null,
            dateLandingCalculation = "18:00",
            airPortTOCode = "LED",
            airPortLACode = "KGD",
            pln = "RA12345"
        ),
        CrewPlanEvent(
            flight = "DP-789",
            status = 0,
            dateTakeoffAirParking = "16:10",
            dateTakeoff = "10.08.24",
            dateTakeoffCalculation = "16:20",
            dateLanding = "18:00",
            dateLandingAirParking = "18:20",
            dateLandingCalculation = "18:00",
            airPortTOCode = "LED",
            airPortLACode = "KGD",
            pln = "RA12345"
        ),
        CrewPlanEvent(
            flight = "Отпуск",
            comment = "comment to vacation, can be too long",
            status = 0
        ),
        CrewPlanEvent(
            flight = "РЕЗерв",
            status = 4,
            comment = "comment to vacation, can be too long"
        )
    )
) {
    SkyPawsTheme {
        val listState = rememberLazyListState()
        // expanding item (additional information)
        val showNotConstantPart = remember { mutableStateMapOf<Int, Boolean>() }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            userScrollEnabled = true

        ) {
            if (list.isNotEmpty()) {
                itemsIndexed(list) { index, item ->
                    Column {
                        // event = flight
                        if (item.flight.startsWith("DP")) {
                            FlightPreview(
                                event = item
                            )

                            // flight is clicked -> additional information is shown
                            if (showNotConstantPart[index] == true) {
                                // flight is in the future
                                if (item.status == 0 && item.dateTakeoffAirParking == null) {
                                    ExpandedDataNotFlownPreview(
                                        event = item
                                    )
                                } else {
                                    // flight in progress or there's CFP
                                    // (flight plan and calculated information)
                                    ExpandedDataFlownPreview(
                                        event = item,
                                    )
                                }
                            }
                            // event != flight (vacation, visit and etc.)
                        } else {
                            EventPreview(
                                event = item,
                            )
                        }

                        // event or flight is clicked -> comments are shown
                        if (showNotConstantPart[index] == true) {
                            if (!item.comment.isNullOrEmpty()) {
                                Comment(event = item)
                            }
                        }
                    }
                }
            }
        }
    }
}