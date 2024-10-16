package ru.skypaws.features.ui.logbookpayment

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.skypaws.presentation.R
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@Composable
fun LogbookPaymentContent(
    list: List<Int>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            color = LocalCustomColors.current.blackWhite,
            text = stringResource(id = R.string.logbook_formats)
        )
        Spacer(modifier = Modifier.height(4.dp))

        Column(
            modifier = Modifier.padding(start = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            list.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        tint = LocalCustomColors.current.blackWhite,
                        painter = painterResource(id = R.drawable.baseline_circle_24),
                        contentDescription = "bulleted icon",
                        modifier = Modifier.size(6.dp)
                    )


                    Text(
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier.padding(start = 12.dp),
                        text = stringResource(id = item),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            color = LocalCustomColors.current.blackWhite,
            text = stringResource(id = R.string.period_text),
        )
        Text(
            color = LocalCustomColors.current.blackWhite,
            modifier = Modifier.padding(start = 12.dp),
            text = pluralStringResource(id = R.plurals.durationSincePaymentDay, count = 1, 1),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))


        Text(
            color = LocalCustomColors.current.blackWhite,
            text = stringResource(id = R.string.logbook_other)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // text height to set icon height as the text
        var textHeight by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.warning),
                contentDescription = "warning icon",
                tint = Color.Red,
                modifier = Modifier
                    .height(textHeight / 2)
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                color = LocalCustomColors.current.blackWhite,
                text = stringResource(id = R.string.logbook_warning),
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    textHeight = with(density) { coordinates.size.height.toDp() }
                }
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
fun LogbookPaymentContentPreview(
    list: List<Int> = listOf(
        R.string.excel,
        R.string.easa,
        R.string.faa
    )
) {
    SkyPawsTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                color = LocalCustomColors.current.blackWhite,
                text = stringResource(id = R.string.logbook_formats)
            )
            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier.padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                list.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            tint = LocalCustomColors.current.blackWhite,
                            painter = painterResource(id = R.drawable.baseline_circle_24),
                            contentDescription = "bulleted icon",
                            modifier = Modifier.size(6.dp)
                        )


                        Text(
                            color = LocalCustomColors.current.blackWhite,
                            modifier = Modifier.padding(start = 12.dp),
                            text = stringResource(id = item),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                color = LocalCustomColors.current.blackWhite,
                text = stringResource(id = R.string.period_text),
            )
            Text(
                color = LocalCustomColors.current.blackWhite,
                modifier = Modifier.padding(start = 12.dp),
                text = pluralStringResource(id = R.plurals.durationSincePaymentDay, count = 1, 1),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))


            Text(
                color = LocalCustomColors.current.blackWhite,
                text = stringResource(id = R.string.logbook_other)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // text height to set icon height as the text
            var textHeight by remember { mutableStateOf(0.dp) }
            val density = LocalDensity.current
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.warning),
                    contentDescription = "warning icon",
                    tint = Color.Red,
                    modifier = Modifier
                        .height(textHeight / 2)
                        .align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    color = LocalCustomColors.current.blackWhite,
                    text = stringResource(id = R.string.logbook_warning),
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        textHeight = with(density) { coordinates.size.height.toDp() }
                    }
                )
            }
        }
    }
}