package ru.skypaws.features.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@Composable
fun MyCard(
    title: Int,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.shadow),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.cards_titles).value.sp,
                    text = stringResource(id = title),
                    color = LocalCustomColors.current.blackWhite,
                    modifier = Modifier.padding(16.dp)
                )

                content()

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {
                    TextButton(
                        onClick = { onCancel() }
                    ) {
                        Text(
                            color = LocalCustomColors.current.blackWhite,
                            fontWeight = FontWeight.Bold,
                            text = stringResource(id = R.string.cancel),
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = { onConfirm() }
                    ) {
                        Text(
                            color = LocalCustomColors.current.blackWhite,
                            fontWeight = FontWeight.Bold,
                            text = stringResource(id = R.string.ok),
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
fun MyCardPreview(
    title: Int = R.string.theme,
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
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.cards_titles).value.sp,
                        text = stringResource(id = title),
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier.padding(16.dp)
                    )

                    RadioButtonGroupPreview()

                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(8.dp)
                    ) {
                        TextButton(
                            onClick = { }
                        ) {
                            Text(
                                color = LocalCustomColors.current.blackWhite,
                                fontWeight = FontWeight.Bold,
                                text = stringResource(id = R.string.cancel),
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = { }
                        ) {
                            Text(
                                color = LocalCustomColors.current.blackWhite,
                                fontWeight = FontWeight.Bold,
                                text = stringResource(id = R.string.ok),
                            )
                        }
                    }
                }
            }
        }
    }
}