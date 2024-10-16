package ru.skypaws.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.presentation.R
import ru.skypaws.presentation.model.UpdateModel
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme


@Composable
fun CardDownloadProgress(updateState: UpdateModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.shadow),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.cards_titles).value.sp,
                    text = stringResource(id = R.string.updating),
                    color = LocalCustomColors.current.blackWhite,
                    modifier = Modifier
                )

                if (updateState.downloadProgress != 0f) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { updateState.downloadProgress / 100f },
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically),
                        )

                        Text(
                            text = "${updateState.downloadProgress.toInt()}%",
                            modifier = Modifier
                                .padding(start = 12.dp)
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .align(Alignment.CenterHorizontally),
                        text = stringResource(id = R.string.downloading),
                        color = LocalCustomColors.current.blackWhite,
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "ru",
    name = "dark"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "ru",
    name = "light"
)
@Composable
fun CardDownloadProgressPreview(
    updateState: UpdateModel = UpdateModel(
        downloadProgress = 75.0f
    )
) {
    SkyPawsTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalCustomColors.current.shadow),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                elevation = CardDefaults.elevatedCardElevation(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.cards_titles).value.sp,
                        text = stringResource(id = R.string.updating),
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier
                    )

                    if (updateState.downloadProgress != 0f) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = { updateState.downloadProgress / 100f },
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterVertically),
                            )

                            Text(
                                text = "${updateState.downloadProgress.toInt()}%",
                                modifier = Modifier
                                    .padding(start = 12.dp)
                            )
                        }
                    } else {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .align(Alignment.CenterHorizontally),
                            text = stringResource(id = R.string.downloading),
                            color = LocalCustomColors.current.blackWhite,
                        )
                    }
                }
            }
        }
    }
}