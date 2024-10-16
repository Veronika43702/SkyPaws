package ru.skypaws.features.ui.crewplan

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.mobileapp.domain.model.CrewPlanEvent
import ru.skypaws.presentation.R
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

// event = flight (constant part)
@Composable
fun Flight(
    event: CrewPlanEvent,
    isClickable: Boolean,
    onClick: () -> Unit,
    getDateFromISO: (String?) -> String,
    getTimeFromISO: (String?) -> String,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(enabled = isClickable) { onClick() }
    ) {

        /* flight status
        0 - planned (in the future) (white)
        1 - in progress (blue)
        2 - accomplished (green)
         */
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 2.dp),
            color = when (event.status) {
                1 -> Color.Cyan
                2 -> Color.Green
                else -> Color.White
            },
            thickness = 8.dp
        )


        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            // flight number (DP-xxx)
            Text(
                text = event.flight,
                color = LocalCustomColors.current.blackWhite,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(id = R.dimen.normal_text).value.sp

            )

            // Flight date
            Text(
                text = getDateFromISO(event.dateTakeoff),
                color = LocalCustomColors.current.blackWhite,
                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
            )
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
                    // departure airport
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 2.dp),
                        text = event.airPortTOCode ?: "",
                        color = LocalCustomColors.current.blackWhite,
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )

                    // departure time (block)
                    Text(
                        // flight in the future, no CFP (no calculated values)
                        text = if (event.status == 0 && event.dateTakeoffAirParking == null) {
                            getTimeFromISO(event.dateTakeoff)
                            // flight accomplished
                        } else if (event.status == 2) {
                            getTimeFromISO(event.dateTakeoffAirParking)
                            // flight in progress or there's CFP (calculated values)
                        } else {
                            getTimeFromISO(event.dateTakeoffCalculation)
                        },
                        modifier = Modifier
                            .weight(0.8f)
                            .padding(start = 2.dp),
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp,
                        color = if (
                        // a delay of departure -> departure time colored red
                            (event.status == 0 || event.status == 1)
                            && event.dateTakeoff != event.dateTakeoffCalculation
                        ) {
                            Color.Red
                        } else {
                            LocalCustomColors.current.blackWhite
                        }
                    )

                    // arrival time (block)
                    Text(
                        //  // flight in the future, no CFP (no calculated values)
                        text = if (event.status == 0 && event.dateTakeoffAirParking == null) {
                            getTimeFromISO(event.dateLanding)
                            // flight accomplished
                        } else if (event.status == 2) {
                            getTimeFromISO(event.dateLandingAirParking)
                            // flight in progress or there's CFP (calculated values)
                        } else {
                            getTimeFromISO(event.dateLandingCalculation)
                        },
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier
                            .weight(0.8f)
                            .padding(start = 2.dp, end = 2.dp),
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )

                    // arrival airport
                    Text(
                        text = event.airPortLACode ?: "",
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 2.dp),
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )
                }


                // aircraft registration number
                Text(
                    text = event.pln ?: "",
                    Modifier.padding(horizontal = 4.dp),
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp,
                    color = LocalCustomColors.current.blackWhite,
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
    event: CrewPlanEvent = CrewPlanEvent(
        flight = "FL-123",
        status = 1,
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
    isClickable: Boolean = true,
) {
    SkyPawsTheme {

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable(enabled = true) { }
        ) {

            /* flight status
            0 - planned (in the future) (white)
            1 - in progress (blue)
            2 - accomplished (green)
             */
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 2.dp),
                color = when (event.status) {
                    1 -> Color.Cyan
                    2 -> Color.Green
                    else -> Color.White
                },
                thickness = 8.dp
            )


            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                // flight number (DP-xxx)
                Text(
                    text = event.flight,
                    color = LocalCustomColors.current.blackWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp

                )

                // Flight date
                Text(
                    text = event.dateTakeoff,
                    color = LocalCustomColors.current.blackWhite,
                    fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                )
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        // departure airport
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 2.dp),
                            text = event.airPortTOCode ?: "",
                            color = LocalCustomColors.current.blackWhite,
                            fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                        )

                        // departure time (block)
                        Text(
                            // flight in the future, no CFP (no calculated values)
                            text = if (event.status == 0 && event.dateTakeoffAirParking == null) {
                                event.dateTakeoff
                                // flight accomplished
                            } else if (event.status == 2) {
                                event.dateTakeoffAirParking ?: ""
                                // flight in progress or there's CFP (calculated values)
                            } else {
                                event.dateTakeoffCalculation ?: ""
                            },
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(start = 2.dp),
                            fontSize = dimensionResource(id = R.dimen.normal_text).value.sp,
                            color = if (
                            // a delay of departure -> departure time colored red
                                (event.status == 0 || event.status == 1)
                                && event.dateTakeoff != event.dateTakeoffCalculation
                            ) {
                                Color.Red
                            } else {
                                LocalCustomColors.current.blackWhite
                            }
                        )

                        // arrival time (block)
                        Text(
                            //  // flight in the future, no CFP (no calculated values)
                            text = if (event.status == 0 && event.dateTakeoffAirParking == null) {
                                event.dateLanding ?: ""
                                // flight accomplished
                            } else if (event.status == 2) {
                                event.dateLandingAirParking ?: ""
                                // flight in progress or there's CFP (calculated values)
                            } else {
                                event.dateLandingCalculation ?: ""
                            },
                            color = LocalCustomColors.current.blackWhite,
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(start = 2.dp, end = 2.dp),
                            fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                        )

                        // arrival airport
                        Text(
                            text = event.airPortLACode ?: "",
                            color = LocalCustomColors.current.blackWhite,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 2.dp),
                            fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                        )
                    }


                    // aircraft registration number
                    Text(
                        text = event.pln ?: "",
                        Modifier.padding(horizontal = 4.dp),
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp,
                        color = LocalCustomColors.current.blackWhite,
                    )
                }
            }
        }
    }
}