package ru.skypaws.features.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.presentation.R
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@Composable
fun RadioButtonGroup(
    optionResourceId: Int,
    optionValue: Int,
    selectedOption: Int?,
    onOptionSelected: (Int) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = optionValue == selectedOption,
            onClick = { onOptionSelected(optionValue) },
            interactionSource = interactionSource,
        )

        Text(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                onClick = { onOptionSelected(optionValue) }
            ),
            text = stringResource(id = optionResourceId),
            color = LocalCustomColors.current.blackWhite,
            fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
        )
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "ru",
    name = "DefaultPreviewLight"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "ru",
    name = "DefaultPreviewDark"
)
@Composable
fun RadioButtonGroupPreview(
    optionResourceId: Int = R.string.light,
    optionValue: Int = 1,
    selectedOption: Int? = 1,
) {
    SkyPawsTheme {
        val interactionSource = remember { MutableInteractionSource() }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = optionValue == selectedOption,
                onClick = { },
                interactionSource = interactionSource,
            )

            Text(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = ripple(bounded = true),
                    onClick = { }
                ),
                text = stringResource(id = optionResourceId),
                color = LocalCustomColors.current.blackWhite,
                fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
            )
        }
    }
}