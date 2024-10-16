package ru.skypaws.features.ui.logbook

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.mobileapp.domain.model.LogbookFlight
import ru.skypaws.presentation.R
import ru.skypaws.features.model.LogbookModel
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme
import ru.skypaws.presentation.ui.theme.flight_flown
import ru.skypaws.presentation.ui.theme.flight_inProgress

@Composable
fun Flight(
    flight: LogbookFlight,
    type: Int,
    state: LogbookModel,
    getDayOfWeek: (String) -> Int,
    getDateNumberFromISO: (String) -> String,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(vertical = 0.5.dp)
            .background(LocalCustomColors.current.logbookFlight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 2.dp),
            color = if (type == 0) {
                flight_flown
            } else {
                flight_inProgress
            },
            thickness = 8.dp
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        ) {
            // flight first line
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val day = when (getDayOfWeek(flight.dateFlight)) {
                    1 -> stringResource(R.string.mon)
                    2 -> stringResource(R.string.tue)
                    3 -> stringResource(R.string.wed)
                    4 -> stringResource(R.string.thu)
                    5 -> stringResource(R.string.fri)
                    6 -> stringResource(R.string.sat)
                    else -> stringResource(R.string.sun)
                }
                Text(
                    color = LocalCustomColors.current.blackWhite,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    text = "${getDateNumberFromISO(flight.dateFlight)} $day",
                    fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                )

                TextLogbookMedium(
                    content = state.flightBlock[flight] ?: "",
                    modifier = Modifier.weight(1f)
                )

                TextLogbookMedium(
                    content = state.flightFlight[flight] ?: "",
                    modifier = Modifier.weight(1f)
                )
                TextLogbookMedium(
                    content = state.flightNight[flight] ?: "",
                    modifier = Modifier.weight(1f)
                )
            }

            // flight second line
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    color = LocalCustomColors.current.blackWhite,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    text = flight.flight,
                    fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                )


                TextLogbookMedium(
                    content = flight.airportTakeoffCode,
                    modifier = Modifier.weight(1f)
                )

                TextLogbookMedium(
                    content = flight.airportLandingCode,
                    modifier = Modifier.weight(1f)
                )

                TextLogbookMedium(
                    content = flight.pln ?: "",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Box(
            modifier = Modifier
                .width(34.dp)
                .align(Alignment.CenterVertically)
        ) {
            if (type == 3) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.baseline_update_24),
                    contentDescription = "expand more"
                )
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
fun FlightPreview(
    flight: LogbookFlight = LogbookFlight(
        flight = "123",
        dateFlight = "10.08.2024",
        airportTakeoffCode = "LED",
        airportLandingCode = "KGD",
        pln = "RA12345"
    ),
    state: LogbookModel = LogbookModel(
        flightBlock = mapOf(flight to "02:30"),
        flightFlight = mapOf(flight to "02:10"),
        flightNight = mapOf(flight to "01:00")
    ),
    type: Int = 0,
    dayInt: Int = 1,
) {
    SkyPawsTheme {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .padding(vertical = 0.5.dp)
                .background(LocalCustomColors.current.logbookFlight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 2.dp),
                color = if (type == 0) {
                    flight_flown
                } else {
                    flight_inProgress
                },
                thickness = 8.dp
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp)
            ) {
                // flight first line
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val day = when (dayInt) {
                        1 -> stringResource(R.string.mon)
                        2 -> stringResource(R.string.tue)
                        3 -> stringResource(R.string.wed)
                        4 -> stringResource(R.string.thu)
                        5 -> stringResource(R.string.fri)
                        6 -> stringResource(R.string.sat)
                        else -> stringResource(R.string.sun)
                    }
                    Text(
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        text = "${flight.dateFlight} $day",
                        fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                    )

                    TextLogbookMedium(
                        content = state.flightBlock[flight] ?: "",
                        modifier = Modifier.weight(1f)
                    )

                    TextLogbookMedium(
                        content = state.flightFlight[flight] ?: "",
                        modifier = Modifier.weight(1f)
                    )
                    TextLogbookMedium(
                        content = state.flightNight[flight] ?: "",
                        modifier = Modifier.weight(1f)
                    )
                }

                // flight second line
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        text = flight.flight,
                        fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                    )


                    TextLogbookMedium(
                        content = flight.airportTakeoffCode,
                        modifier = Modifier.weight(1f)
                    )

                    TextLogbookMedium(
                        content = flight.airportLandingCode,
                        modifier = Modifier.weight(1f)
                    )

                    TextLogbookMedium(
                        content = flight.pln ?: "",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(34.dp)
                    .align(Alignment.CenterVertically)
            ) {
                if (type == 3) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center),
                        painter = painterResource(id = R.drawable.baseline_update_24),
                        contentDescription = "expand more"
                    )
                }
            }
        }
    }
}