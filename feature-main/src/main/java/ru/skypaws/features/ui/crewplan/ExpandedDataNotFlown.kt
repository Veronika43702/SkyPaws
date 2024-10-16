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
fun ExpandedDataNotFlown(
    event: CrewPlanEvent,
    getTimeFromISO: (String?) -> String,
    getTimeBetween: (String?, String?) -> String
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
                    .fillMaxHeight()
                    .padding(end = 12.dp),
                color = MaterialTheme.colorScheme.background,
                thickness = 8.dp
            )

            Column(
                modifier = Modifier.padding(vertical = 4.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // calculated (there's CFP) or planned name (word) for time in line (row)
                    Text(
                        modifier = Modifier
                            .weight(0.4f)
                            .padding(start = 4.dp),
                        text = if (event.dateTakeoff != event.dateTakeoffCalculation || event.dateLanding != event.dateLandingCalculation) {
                            stringResource(R.string.calculated)
                        } else {
                            stringResource(R.string.planned)
                        },
                        color = LocalCustomColors.current.blackWhite,
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .weight(1f),
                    ) {

                        // calculated (planned) departure time
                        Text(
                            modifier = Modifier
                                .padding(start = 12.dp),
                            text = getTimeFromISO(event.dateTakeoffCalculation),
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )

                        // word "block/flight". There's CFP (calculations) -> flight, else -> block
                        Text(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 1.dp),
                            text = if (event.dateTakeoff != event.dateTakeoffCalculation || event.dateLanding != event.dateLandingCalculation) {
                                stringResource(id = R.string.flight)
                            } else {
                                stringResource(id = R.string.block)
                            },
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )

                        // calculated/planned flight time
                        Text(
                            text = getTimeBetween(
                                event.dateTakeoffCalculation,
                                event.dateLandingCalculation
                            ),
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )

                        // calculated/planned arrival time
                        Text(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 12.dp),
                            text = getTimeFromISO(event.dateLandingCalculation),
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
fun ExpandedDataNotFlownPreview(
    event: CrewPlanEvent = CrewPlanEvent(),
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
                        .fillMaxHeight()
                        .padding(end = 12.dp),
                    color = MaterialTheme.colorScheme.background,
                    thickness = 8.dp
                )

                Column(
                    modifier = Modifier.padding(vertical = 4.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // calculated (there's CFP) or planned name (word) for time in line (row)
                        Text(
                            modifier = Modifier
                                .weight(0.4f)
                                .padding(start = 4.dp),
                            text = if (event.dateTakeoff != event.dateTakeoffCalculation || event.dateLanding != event.dateLandingCalculation) {
                                stringResource(R.string.calculated)
                            } else {
                                stringResource(R.string.planned)
                            },
                            color = LocalCustomColors.current.blackWhite,
                            fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .weight(1f),
                        ) {

                            // calculated (planned) departure time
                            Text(
                                modifier = Modifier
                                    .padding(start = 12.dp),
                                text = "16:10",
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )

                            // word "block/flight". There's CFP (calculations) -> flight, else -> block
                            Text(
                                modifier = Modifier
                                    .padding(start = 12.dp, end = 1.dp),
                                text = if (event.dateTakeoff != event.dateTakeoffCalculation || event.dateLanding != event.dateLandingCalculation) {
                                    stringResource(id = R.string.flight)
                                } else {
                                    stringResource(id = R.string.block)
                                },
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )

                            // calculated/planned flight time
                            Text(
                                text = "02:40",
                                color = LocalCustomColors.current.blackWhite,
                                maxLines = 1,
                                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                            )

                            // calculated/planned arrival time
                            Text(
                                modifier = Modifier
                                    .padding(start = 12.dp, end = 12.dp),
                                text = "18:50",
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