package ru.skypaws.features.ui.logbook

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.presentation.R
import ru.skypaws.features.model.LogbookModel
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme
import ru.skypaws.presentation.ui.theme.flight_flown
import ru.skypaws.presentation.ui.theme.transparent

@Composable
fun Total(
    state: LogbookModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalCustomColors.current.logbookTotal)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Row {
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 2.dp),
                    color = transparent,
                    thickness = 8.dp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets(top = 4.dp)),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                // Total blank
                Text(
                    color = LocalCustomColors.current.blackWhite,
                    modifier = Modifier.weight(1f),
                    text = "",
                )

                TextLogbookMediumUpperCase(
                    id = R.string.block,
                    modifier = Modifier.weight(1f)
                )

                TextLogbookMediumUpperCase(
                    id = R.string.flight,
                    modifier = Modifier.weight(1f)
                )
                TextLogbookMediumUpperCase(
                    id = R.string.night,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .width(34.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Row {
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 2.dp),
                    color = flight_flown,
                    thickness = 8.dp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets(top = 4.dp)),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                // Total
                Text(
                    color = LocalCustomColors.current.blackWhite,
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(id = R.string.total).uppercase(),
                    fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                )

                TextLogbookMedium(
                    content = state.totalBlock,
                    modifier = Modifier.weight(1f)
                )

                TextLogbookMedium(
                    content = state.totalFlight,
                    modifier = Modifier.weight(1f)
                )

                TextLogbookMedium(
                    content = state.totalNight,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .width(34.dp)
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
fun TotalPreview(
    state: LogbookModel = LogbookModel(
        totalBlock = "1000:00",
        totalFlight = "900:00",
        totalNight = "700:00"
    )
) {
    SkyPawsTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalCustomColors.current.logbookTotal)
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Row {
                    VerticalDivider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 2.dp),
                        color = transparent,
                        thickness = 8.dp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets(top = 4.dp)),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    // Total blank
                    Text(
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier.weight(1f),
                        text = "",
                    )

                    TextLogbookMediumUpperCase(
                        id = R.string.block,
                        modifier = Modifier.weight(1f)
                    )

                    TextLogbookMediumUpperCase(
                        id = R.string.flight,
                        modifier = Modifier.weight(1f)
                    )
                    TextLogbookMediumUpperCase(
                        id = R.string.night,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .width(34.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Row {
                    VerticalDivider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 2.dp),
                        color = flight_flown,
                        thickness = 8.dp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets(top = 4.dp)),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    // Total
                    Text(
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier
                            .weight(1f),
                        text = stringResource(id = R.string.total).uppercase(),
                        fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                    )

                    TextLogbookMedium(
                        content = state.totalBlock,
                        modifier = Modifier.weight(1f)
                    )

                    TextLogbookMedium(
                        content = state.totalFlight,
                        modifier = Modifier.weight(1f)
                    )

                    TextLogbookMedium(
                        content = state.totalNight,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .width(34.dp)
                    )
                }
            }
        }
    }
}