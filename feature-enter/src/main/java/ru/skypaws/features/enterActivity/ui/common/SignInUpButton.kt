package ru.skypaws.features.enterActivity.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ru.skypaws.presentation.R
import ru.skypaws.features.enterActivity.model.EnterModel

@Composable
internal fun SignInUpButton(
    enterState: EnterModel,
    button: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(0.9f),
            textAlign = TextAlign.Center,
            text = if (enterState.registerError || enterState.signInError) {
                when {
                    enterState.wrongData -> stringResource(R.string.wrongEmailPass)
                    enterState.userNotFound -> stringResource(R.string.wrongEmailPass)
                    enterState.networkError -> stringResource(R.string.serverNotWork)
                    enterState.aviabitServerTimeOut -> stringResource(R.string.aviabitNotWork)
                    else -> ""
                }
            } else "",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.error
        )

        button()
    }
}