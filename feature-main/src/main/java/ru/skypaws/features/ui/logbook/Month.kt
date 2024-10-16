package ru.skypaws.features.ui.logbook

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.mobileapp.domain.model.MonthType
import ru.skypaws.mobileapp.domain.model.YearMonthType
import ru.skypaws.presentation.R
import ru.skypaws.features.model.LogbookModel
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme
import ru.skypaws.presentation.ui.theme.flight_flown
import ru.skypaws.presentation.ui.theme.flight_inProgress

@Composable
fun Month(
    yearMonthType: YearMonthType,
    state: LogbookModel,
    isClicked: Boolean,
    onClick: () -> Unit
) {
    // Animate the rotation angle
    val rotationAngle by animateFloatAsState(
        targetValue = if (isClicked) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "expand arrow"
    )

    // month
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(vertical = 0.5.dp)
            .background(LocalCustomColors.current.logbookMonth)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Row {
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 2.dp),
                color = if (yearMonthType.type == 0) {
                    flight_flown
                } else {
                    flight_inProgress
                },
                thickness = 8.dp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets(top = 4.dp, bottom = 4.dp)),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Text(
                color = LocalCustomColors.current.blackWhite,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
                text = when (yearMonthType.month) {
                    1 -> stringResource(R.string.jan)
                    2 -> stringResource(R.string.feb)
                    3 -> stringResource(R.string.mar)
                    4 -> stringResource(R.string.apr)
                    5 -> stringResource(R.string.may)
                    6 -> stringResource(R.string.jun)
                    7 -> stringResource(R.string.jul)
                    8 -> stringResource(R.string.aug)
                    9 -> stringResource(R.string.sep)
                    10 -> stringResource(R.string.oct)
                    11 -> stringResource(R.string.nov)
                    else -> stringResource(R.string.dec)
                },
                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
            )

            TextLogbookMedium(
                content = state.totalBlockByMonth[yearMonthType] ?: "",
                modifier = Modifier.weight(1f)
            )

            TextLogbookMedium(
                content = state.totalFlightByMonth[yearMonthType] ?: "",
                modifier = Modifier.weight(1f)
            )

            TextLogbookMedium(
                content = state.totalNightByMonth[yearMonthType] ?: "",
                modifier = Modifier.weight(1f)
            )


            Box(
                modifier = Modifier
                    .width(34.dp)
            ) {
                Icon(
                    tint = LocalCustomColors.current.blackWhite,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                        .graphicsLayer {
                            rotationZ = rotationAngle
                        },
                    painter = painterResource(id = R.drawable.expand_more),
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
fun MonthPreview(
    yearMonthType: YearMonthType = YearMonthType(2024, 8, 3),
    month: MonthType = MonthType(8, 1),
    state: LogbookModel = LogbookModel(
        totalBlockByMonth = mapOf(yearMonthType to "85:00"),
        totalFlightByMonth = mapOf(yearMonthType to "70:30"),
        totalNightByMonth = mapOf(yearMonthType to "30:10")
    ),
    isClicked: Boolean = false
) {
    SkyPawsTheme {
        // Animate the rotation angle
        val rotationAngle by animateFloatAsState(
            targetValue = if (isClicked) 180f else 0f,
            animationSpec = tween(durationMillis = 300),
            label = "expand arrow"
        )

        // month
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .padding(vertical = 0.5.dp)
                .background(LocalCustomColors.current.logbookMonth)
                .clickable {},
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Row {
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 2.dp),
                    color = if (month.type == 0) {
                        flight_flown
                    } else {
                        flight_inProgress
                    },
                    thickness = 8.dp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets(top = 4.dp, bottom = 4.dp)),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    color = LocalCustomColors.current.blackWhite,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    text = when (month.month) {
                        1 -> stringResource(R.string.jan)
                        2 -> stringResource(R.string.feb)
                        3 -> stringResource(R.string.mar)
                        4 -> stringResource(R.string.apr)
                        5 -> stringResource(R.string.may)
                        6 -> stringResource(R.string.jun)
                        7 -> stringResource(R.string.jul)
                        8 -> stringResource(R.string.aug)
                        9 -> stringResource(R.string.sep)
                        10 -> stringResource(R.string.oct)
                        11 -> stringResource(R.string.nov)
                        else -> stringResource(R.string.dec)
                    },
                    fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                )

                TextLogbookMedium(
                    content = state.totalBlockByMonth[yearMonthType] ?: "",
                    modifier = Modifier.weight(1f)
                )

                TextLogbookMedium(
                    content = state.totalFlightByMonth[yearMonthType] ?: "",
                    modifier = Modifier.weight(1f)
                )

                TextLogbookMedium(
                    content = state.totalNightByMonth[yearMonthType] ?: "",
                    modifier = Modifier.weight(1f)
                )


                Box(
                    modifier = Modifier
                        .width(34.dp)
                ) {
                    Icon(
                        tint = LocalCustomColors.current.blackWhite,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                            .graphicsLayer {
                                rotationZ = rotationAngle
                            },
                        painter = painterResource(id = R.drawable.expand_more),
                        contentDescription = "expand more"
                    )
                }
            }
        }
    }
}