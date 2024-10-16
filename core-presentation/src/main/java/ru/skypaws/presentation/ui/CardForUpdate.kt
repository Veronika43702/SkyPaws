package ru.skypaws.presentation.ui

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
fun CardForUpdate(update: () -> Unit) {
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
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.cards_titles).value.sp,
                    text = stringResource(id = R.string.update_app),
                    color = LocalCustomColors.current.blackWhite,
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.update_forced_explanation),
                    color = LocalCustomColors.current.blackWhite,
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {

                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = { update() }
                    ) {
                        Text(
                            color = LocalCustomColors.current.textButton,
                            fontWeight = FontWeight.Bold,
                            text = stringResource(id = R.string.update).uppercase(),
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
    name = "dark"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "ru",
    name = "light"
)
@Composable
fun CardForUpdatePreview() {
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
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.cards_titles).value.sp,
                        text = stringResource(id = R.string.update_app),
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier.padding(16.dp)
                    )

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.update_forced_explanation),
                        color = LocalCustomColors.current.blackWhite,
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(8.dp)
                    ) {

                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {  }
                        ) {
                            Text(
                                color = LocalCustomColors.current.textButton,
                                fontWeight = FontWeight.Bold,
                                text = stringResource(id = R.string.update).uppercase(),
                            )
                        }
                    }
                }
            }
        }
    }
}