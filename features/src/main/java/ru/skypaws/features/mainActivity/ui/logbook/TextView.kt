package ru.skypaws.features.mainActivity.ui.logbook

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ru.skypaws.features.R
import ru.skypaws.features.ui.theme.LocalCustomColors

@Composable
fun TextLogbookMediumUpperCase(id: Int, modifier: Modifier) {
    Text(
        color = LocalCustomColors.current.blackWhite,
        modifier = modifier,
        text = stringResource(id).uppercase(),
        textAlign = TextAlign.End,
        fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
    )
}

@Composable
fun TextLogbookMedium(content: String, modifier: Modifier) {
    Text(
        color = LocalCustomColors.current.blackWhite,
        modifier = modifier,
        text = content,
        textAlign = TextAlign.End,
        fontSize = dimensionResource(id = R.dimen.medium_text).value.sp
    )
}