package ru.skypaws.features.ui.logbook

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.mobileapp.domain.model.YearType
import ru.skypaws.presentation.R
import ru.skypaws.features.model.LogbookModel
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme
import ru.skypaws.presentation.ui.theme.flight_flown
import ru.skypaws.presentation.ui.theme.flight_inProgress

@Composable
fun Year(
    year: YearType,
    state: LogbookModel,
    isClicked: Boolean
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isClicked) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "expand arrow"
    )

    // year
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {


        Row {
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 2.dp),
                color = if (year.type == 0) {
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
                .padding(
                    top = 4.dp, bottom = if (isClicked) {
                        0.dp
                    } else {
                        4.dp
                    }
                ),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                color = LocalCustomColors.current.blackWhite,
                modifier = Modifier
                    .weight(1f),
                text = year.year.toString(),
                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
            )

            TextLogbookMedium(
                content = state.totalBlockByYear[year.year] ?: "",
                modifier = Modifier.weight(1f)
            )

            TextLogbookMedium(
                content = state.totalFlightByYear[year.year] ?: "",
                modifier = Modifier.weight(1f)
            )

            TextLogbookMedium(
                content = state.totalNightByYear[year.year] ?: "",
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
fun YearPreview(
    isClicked: Boolean = false,
    year: YearType = YearType(2024, 1),
    state: LogbookModel = LogbookModel(
        totalBlockByYear = mapOf(2024 to "300"),
        totalFlightByYear = mapOf(2024 to "500"),
        totalNightByYear = mapOf(2024 to "600"),
    )
) {
    SkyPawsTheme {
        val rotationAngle by animateFloatAsState(
            targetValue = if (isClicked) 180f else 0f,
            animationSpec = tween(durationMillis = 300),
            label = "expand arrow"
        )

        // year
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Row {
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 2.dp),
                    color = if (year.type == 0) {
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
                    .padding(
                        top = 4.dp, bottom = if (isClicked) {
                            0.dp
                        } else {
                            4.dp
                        }
                    ),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    color = LocalCustomColors.current.blackWhite,
                    modifier = Modifier
                        .weight(1f),
                    text = year.year.toString(),
                    fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
                )

                TextLogbookMedium(
                    content = state.totalBlockByYear[year.year] ?: "",
                    modifier = Modifier.weight(1f)
                )

                TextLogbookMedium(
                    content = state.totalFlightByYear[year.year] ?: "",
                    modifier = Modifier.weight(1f)
                )

                TextLogbookMedium(
                    content = state.totalNightByYear[year.year] ?: "",
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