package ru.skypaws.features.ui.crewplan

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.mobileapp.domain.model.CrewPlanEvent
import ru.skypaws.presentation.R
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@Composable
fun Comment(event: CrewPlanEvent) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            HorizontalDivider(
                Modifier.width(8.dp),
                color = MaterialTheme.colorScheme.background,
                thickness = 1.dp
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 1.dp
            )
        }

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

            Text(
                text = event.comment ?: "",
                modifier = Modifier
                    .padding(4.dp),
                color = LocalCustomColors.current.blackWhite,
                fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
            )

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
fun CommentPreview(
    event: CrewPlanEvent = CrewPlanEvent(
        comment = "Comment info"
    )
) {
    SkyPawsTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            ) {
                HorizontalDivider(
                    Modifier.width(8.dp),
                    color = MaterialTheme.colorScheme.background,
                    thickness = 1.dp
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 1.dp
                )
            }

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

                Text(
                    text = event.comment ?: "",
                    modifier = Modifier
                        .padding(4.dp),
                    color = LocalCustomColors.current.blackWhite,
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                )

            }
        }
    }
}