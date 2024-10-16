package ru.skypaws.features.ui.crewplan

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.mobileapp.domain.model.CrewPlanEvent
import ru.skypaws.presentation.R
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@Composable
fun ExpandedDataFlown(
    event: CrewPlanEvent,
    getTimeFromISO: (String?) -> String,
    getTimeBetween: (String?, String?) -> String,
    getISOTimeLandingCalculated: (String?, String?, String?) -> String,
    addMinutesToTime: (String?, Long) -> String,
    getTimeLandingCalculated: (String?, String?, String?) -> String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp
        )

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .background(LocalCustomColors.current.flightBackgroundExpand)
        ) {
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight(),
                color = MaterialTheme.colorScheme.background,
                thickness = 8.dp
            )

            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // first line (flight)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // ATT / ETT (Actual/Estimated Take-off Time)
                    Row(
                        modifier = Modifier
                            .weight(0.35f),
                    ) {
                        // ATT/ETT word
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            text = if (event.dateTakeoffReal != null) {
                                stringResource(id = R.string.ATT)
                            } else {
                                stringResource(id = R.string.ETT)
                            },
                            color = LocalCustomColors.current.blackWhite,
                            fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                        )

                        // take-off time
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            text = if (event.dateTakeoffReal != null) {
                                getTimeFromISO(event.dateTakeoffReal)
                            } else {
                                getTimeFromISO(event.dateTakeoffAirParking)
                            },
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )
                    }

                    // flight time info
                    Row(
                        modifier = Modifier
                            .weight(0.6f),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        // flight word
                        Text(
                            text = stringResource(id = R.string.flight),
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )

                        // flight time (actual if there's actual time or calculated)
                        Text(

                            modifier = Modifier
                                .padding(start = 4.dp),
                            text = if (event.dateLandingReal != null) {
                                // if landed -> duration = Real LDG - TO
                                getTimeBetween(
                                    event.dateTakeoffReal,
                                    event.dateLandingReal
                                )
                            } else {
                                // if not landed -> duration = Calculated LDG - TO
                                getTimeBetween(
                                    event.dateTakeoffCalculation,
                                    event.dateLandingCalculation
                                )
                            },
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )
                    }

                    // ALT / ELT (Actual/Estimated Landing Time)
                    Row(
                        modifier = Modifier
                            .weight(0.35f),
                    ) {
                        Text(
                            text = if (event.dateLandingReal != null) {
                                // if landed -> actual (real) LDG time
                                getTimeFromISO(event.dateLandingReal)
                            } else {
                                // if not landed -> LDG time = (RealTO + Calculated duration)
                                getTimeFromISO(
                                    getISOTimeLandingCalculated(
                                        event.dateTakeoffReal,
                                        event.dateTakeoffCalculation,
                                        event.dateLandingCalculation
                                    )
                                )
                            },
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )

                        // ALT / ELT name
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            text = if (event.dateLandingReal != null) {
                                stringResource(id = R.string.ALT)
                            } else {
                                stringResource(id = R.string.ELT)
                            },
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )
                    }
                }

                // row of horizontal dividers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(0.35f),
                        color = LocalCustomColors.current.flightBackgroundExpand,
                        thickness = 1.dp
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .weight(0.6f),
                        color = LocalCustomColors.current.blackWhite,
                        thickness = 1.dp
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .weight(0.35f),
                        color = LocalCustomColors.current.flightBackgroundExpand,
                        thickness = 1.dp
                    )
                }

                // second line (block)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // ATD (Actual Time of Departure)
                    // (if flown (status 1 или 2) always Actual)
                    Row(
                        modifier = Modifier
                            .weight(0.35f)
                    ) {
                        //ATD word
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            color = LocalCustomColors.current.blackWhite,
                            text = stringResource(id = R.string.ATD),
                            fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                        )

                        // actual departure time
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            text = if (event.dateTakeoffAirParking != null) {
                                getTimeFromISO(event.dateTakeoffAirParking)
                            } else {
                                getTimeFromISO(event.dateTakeoffCalculation)
                            },
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )
                    }

                    // block
                    Row(
                        modifier = Modifier
                            .weight(0.6f),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.block),
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )

                        // Block time - from engine start-up until cutoff

                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            // flight in progress (status 0 but no TO or status 1)
                            text = if (event.status != 2) {
                                // TO accomplished
                                if (event.dateTakeoffReal != null) {
                                    // LDG accomplished
                                    if (event.dateLandingReal != null) {
                                        // block (ISO) = ALT + 10 min (actual landing time + 10 min)
                                        val blockLDG =
                                            addMinutesToTime(event.dateLandingReal, 10)
                                        // block duration = (ALT (actual landing time) + 10 мин) - ATD (engine start-up time)
                                        getTimeBetween(event.dateTakeoffAirParking, blockLDG)
                                        // no LDG (flight in progress)
                                    } else {
                                        // Full format (ISO) of calculated LDG time (ELT):
                                        // RealTO + calculated duration according to CFP
                                        val timeISOEndCalculated =
                                            getISOTimeLandingCalculated(
                                                event.dateTakeoffReal,
                                                event.dateTakeoffCalculation,
                                                event.dateLandingCalculation
                                            )

                                        // block (ISO) = ELT + 10 min (estimated landing time + 10 min)
                                        val blockLDG =
                                            addMinutesToTime(timeISOEndCalculated, 10)
                                        // block duration = (ELT + 10 мин ) - ATD
                                        if (event.dateTakeoffAirParking != null) {
                                            getTimeBetween(event.dateTakeoffAirParking, blockLDG)
                                        } else {
                                            getTimeBetween(event.dateTakeoffCalculation, blockLDG)
                                        }
                                    }
                                    // no TO (taxiing after start-up)
                                } else {
                                    // block duration = calculated flight time according to CFP
                                    getTimeBetween(
                                        event.dateTakeoffCalculation,
                                        event.dateLandingCalculation
                                    )
                                }
                                // flight accomplished
                            } else {
                                getTimeBetween(
                                    event.dateTakeoffAirParking,
                                    event.dateLandingAirParking
                                )
                            },
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )
                    }

                    // ETA / ATA (Estimated/Actual time of Arrival)
                    Row(
                        modifier = Modifier
                            .weight(0.35f)
                    ) {
                        Text(
                            // flight in progress (status 0, no TO or status 1)
                            text = if (event.status != 2) {
                                // TO accomplished
                                if (event.dateTakeoffReal != null) {
                                    // LDG accomplished
                                    if (event.dateLandingReal != null) {
                                        // block (ISO) = ALT + 10 min
                                        val blockLDG =
                                            addMinutesToTime(event.dateLandingReal, 10)
                                        // arrival time = ALT + 10 min
                                        getTimeFromISO(blockLDG)
                                        // no LDG
                                    } else {
                                        // full format of в ELT:
                                        // RealTO + calculated duration according to CFP
                                        val timeISOEndCalculated =
                                            getISOTimeLandingCalculated(
                                                event.dateTakeoffReal,
                                                event.dateTakeoffCalculation,
                                                event.dateLandingCalculation
                                            )

                                        // block (ISO) = ELT + 10 min
                                        val blockLDG =
                                            addMinutesToTime(timeISOEndCalculated, 10)
                                        // arrival time = ELT + 10 мин
                                        getTimeFromISO(blockLDG)
                                    }
                                    // no TO (status 1)
                                } else {
                                    // arrival = departure (ATD) + calculated duration
                                    // engine start-up (airParking) + calculated duration according to CFP
                                    getTimeLandingCalculated(
                                        event.dateTakeoffAirParking,
                                        event.dateTakeoffCalculation,
                                        event.dateLandingCalculation
                                    )
                                }
                                // flight accomplished (status = 2) - actual block
                            } else {
                                getTimeFromISO(event.dateLandingAirParking)
                            },
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )

                        //ETA / ATA word
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            // flight accomplished
                            text = if (event.status == 2) {
                                stringResource(id = R.string.ATA)
                                // flight in progress
                            } else {
                                stringResource(id = R.string.ETA)
                            },
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )


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
fun ExpandedDataFlownPreview(
    event: CrewPlanEvent = CrewPlanEvent(
        dateTakeoffReal = ""
    ),
) {
    SkyPawsTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 1.dp
            )

            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth()
                    .background(LocalCustomColors.current.flightBackgroundExpand)
            ) {
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight(),
                    color = MaterialTheme.colorScheme.background,
                    thickness = 8.dp
                )

                Column(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // first line (flight)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // ATT / ETT (Actual/Estimated Take-off Time)
                        Row(
                            modifier = Modifier
                                .weight(0.35f),
                        ) {
                            // ATT/ETT word
                            Text(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                text = if (event.dateTakeoffReal != null) {
                                    stringResource(id = R.string.ATT)
                                } else {
                                    stringResource(id = R.string.ETT)
                                },
                                color = LocalCustomColors.current.blackWhite,
                                fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                            )

                            // take-off time
                            Text(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                text = "16:10",
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )
                        }

                        // flight time info
                        Row(
                            modifier = Modifier
                                .weight(0.6f),
                            horizontalArrangement = Arrangement.Center
                        ) {

                            // flight word
                            Text(
                                text = stringResource(id = R.string.flight),
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )

                            // flight time (actual if there's actual time or calculated)
                            Text(

                                modifier = Modifier
                                    .padding(start = 4.dp),
                                text = "02:30",
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )
                        }

                        // ALT / ELT (Actual/Estimated Landing Time)
                        Row(
                            modifier = Modifier
                                .weight(0.35f),
                        ) {
                            Text(
                                text = "18:40",
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )

                            // ALT / ELT name
                            Text(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                text = if (event.dateLandingReal != null) {
                                    stringResource(id = R.string.ALT)
                                } else {
                                    stringResource(id = R.string.ELT)
                                },
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )
                        }
                    }

                    // row of horizontal dividers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(0.35f),
                            color = LocalCustomColors.current.flightBackgroundExpand,
                            thickness = 1.dp
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .weight(0.6f),
                            color = LocalCustomColors.current.blackWhite,
                            thickness = 1.dp
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .weight(0.35f),
                            color = LocalCustomColors.current.flightBackgroundExpand,
                            thickness = 1.dp
                        )
                    }

                    // second line (block)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // ATD (Actual Time of Departure)
                        // (if flown (status 1 или 2) always Actual)
                        Row(
                            modifier = Modifier
                                .weight(0.35f)
                        ) {
                            //ATD word
                            Text(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                color = LocalCustomColors.current.blackWhite,
                                text = stringResource(id = R.string.ATD),
                                fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                            )

                            // actual departure time
                            Text(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                text = "16:00",
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )
                        }

                        // block
                        Row(
                            modifier = Modifier
                                .weight(0.6f),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.block),
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )

                            // Block time - from engine start-up until cutoff

                            Text(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                // flight in progress (status 0 but no TO or status 1)
                                text = "02:50",
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )
                        }

                        // ETA / ATA (Estimated/Actual time of Arrival)
                        Row(
                            modifier = Modifier
                                .weight(0.35f)
                        ) {
                            Text(
                                // flight in progress (status 0, no TO or status 1)
                                text = "18:50",
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )

                            //ETA / ATA word
                            Text(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                // flight accomplished
                                text = if (event.status == 2) {
                                    stringResource(id = R.string.ATA)
                                    // flight in progress
                                } else {
                                    stringResource(id = R.string.ETA)
                                },
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )


                        }
                    }
                }
            }
        }
    }
}
