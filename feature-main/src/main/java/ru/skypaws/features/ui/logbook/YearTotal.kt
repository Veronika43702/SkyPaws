package ru.skypaws.features.ui.logbook

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import ru.skypaws.mobileapp.domain.model.YearType
import ru.skypaws.presentation.R
import ru.skypaws.features.model.LogbookModel
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme
import ru.skypaws.presentation.ui.theme.flight_flown
import ru.skypaws.presentation.ui.theme.flight_inProgress

@Composable
fun YearTotal(
    year: YearType,
    state: LogbookModel,
) {
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
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

        }

        Text(
            color = LocalCustomColors.current.blackWhite,
            modifier = Modifier
                .weight(1f),
            text = stringResource(id = R.string.total),
            fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
        )

        TextLogbookMedium(
            content = state.totalBlockWithYear[year.year] ?: "",
            modifier = Modifier.weight(1f)
        )

        TextLogbookMedium(
            content = state.totalFlightWithYear[year.year] ?: "",
            modifier = Modifier.weight(1f)
        )

        TextLogbookMedium(
            content = state.totalNightWithYear[year.year] ?: "",
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .width(34.dp)
        )
    }
}


@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    locale = "ru"
)
@Composable
fun YearTotalPreview(
    year: YearType = YearType(2024, 1),
    state: LogbookModel = LogbookModel(
        totalBlockWithYear = mapOf(2024 to "900:12"),
        totalFlightWithYear = mapOf(2024 to "750:00"),
        totalNightWithYear = mapOf(2024 to "300:00"),
    )
) {
    SkyPawsTheme {
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
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

            }

            Text(
                color = LocalCustomColors.current.blackWhite,
                modifier = Modifier
                    .weight(1f),
                text = stringResource(id = R.string.total),
                fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
            )

            TextLogbookMedium(
                content = state.totalBlockWithYear[year.year] ?: "",
                modifier = Modifier.weight(1f)
            )

            TextLogbookMedium(
                content = state.totalFlightWithYear[year.year] ?: "",
                modifier = Modifier.weight(1f)
            )

            TextLogbookMedium(
                content = state.totalNightWithYear[year.year] ?: "",
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .width(34.dp)
            )
        }
    }
}