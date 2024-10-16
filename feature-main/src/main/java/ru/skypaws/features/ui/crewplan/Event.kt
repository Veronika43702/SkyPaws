package ru.skypaws.features.ui.crewplan

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.mobileapp.domain.model.CrewPlanEvent
import ru.skypaws.presentation.R
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme
import ru.skypaws.presentation.ui.theme.flight_status0
import ru.skypaws.presentation.ui.theme.flight_status4

// event != flight (vacation, visit and etc.)
@Composable
fun Event(
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
            .clickable(enabled = isClickable) {
                if (event.flight != "Доступен для планирования" || !event.flight.contains("РЕЗ")) {
                    onClick()
                }
            }
    ) {

        /* event status
        0 - vacation (purple)
        4 - else (orange)
         */
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 2.dp),
            color = when (event.status) {
                0 -> flight_status0
                4 -> flight_status4
                else -> Color.White
            },
            thickness = 8.dp
        )

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // event name
                Text(
                    text = event.flight,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = LocalCustomColors.current.blackWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp

                )

                // event comments except (Доступен для планирования и Резерва)
                if (event.flight != "Доступен для планирования" && !event.flight.contains("РЕЗ")) {
                    Text(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 2.dp),
                        text = event.comment ?: "",
                        color = LocalCustomColors.current.blackWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // start date
                Text(
                    text = getDateFromISO(event.dateTakeoff),
                    color = LocalCustomColors.current.blackWhite,
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                )
                // start time
                Text(
                    text = getTimeFromISO(event.dateTakeoff),
                    color = LocalCustomColors.current.blackWhite,
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                )
                // end time
                Text(
                    text = getTimeFromISO(event.dateLanding),
                    color = LocalCustomColors.current.blackWhite,
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                )
                // end date
                Text(
                    text = getDateFromISO(event.dateLanding),
                    color = LocalCustomColors.current.blackWhite,
                    modifier = Modifier
                        .padding(end = 2.dp),
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
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
fun EventPreview(
    isClickable: Boolean = true,
    event: CrewPlanEvent = CrewPlanEvent(
        flight = "отпуск",
        status = 0
    ),
) {
    SkyPawsTheme {

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable(enabled = true) {
                    if (event.flight != "Доступен для планирования" || !event.flight.contains("РЕЗ")) {
                        println("expanded")
                    }
                }
        ) {

        /*
            event status
            0 - vacation (purple)
            4 - else (orange)
        */
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 2.dp),
                color = when (event.status) {
                    0 -> flight_status0
                    4 -> flight_status4
                    else -> Color.White
                },
                thickness = 8.dp
            )

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // event name
                    Text(
                        text = event.flight,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = LocalCustomColors.current.blackWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp

                    )

                    // event comments except (Доступен для планирования и Резерва)
                    if (event.flight != "Доступен для планирования" && !event.flight.contains("РЕЗ")) {
                        Text(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 2.dp),
                            text = event.comment ?: "",
                            color = LocalCustomColors.current.blackWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // start date
                    Text(
                        text = "10.08.2024",
                        color = LocalCustomColors.current.blackWhite,
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )
                    // start time
                    Text(
                        text = "16:10",
                        color = LocalCustomColors.current.blackWhite,
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )
                    // end time
                    Text(
                        text = "18:40",
                        color = LocalCustomColors.current.blackWhite,
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )
                    // end date
                    Text(
                        text = "10.08.2024",
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier
                            .padding(end = 2.dp),
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )
                }
            }
        }
    }
}