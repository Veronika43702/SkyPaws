package ru.skypaws.features.enterActivity.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.presentation.R
import ru.skypaws.features.enterActivity.model.EnterModel

@Composable
internal fun CodeButton(
    enterDataState: EnterModel,
    getResponseForCode: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = if (enterDataState.codeSendingError) {
                when {
                    enterDataState.wrongData -> stringResource(R.string.wrongEmailPass)
                    enterDataState.userNotFound -> stringResource(R.string.userNotFound)
                    enterDataState.networkError -> stringResource(R.string.serverNotWork)
                    enterDataState.aviabitServerTimeOut -> stringResource(R.string.aviabitNotWork)
                    else -> ""
                }
            } else if (enterDataState.codeIsSent) {
                stringResource(R.string.code_sent)
            } else "",
            color = if (enterDataState.codeIsSent) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            },
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(0.9f)
        )

        Button(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp),
            onClick = { getResponseForCode() }
        ) {
            Text(text = stringResource(R.string.send_code))
        }
    }
}


@Composable
internal fun CodeButtonPreview(
    enterDataState: EnterModel = EnterModel()
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = if (enterDataState.codeSendingError) {
                when {
                    enterDataState.wrongData -> stringResource(R.string.wrongEmailPass)
                    enterDataState.userNotFound -> stringResource(R.string.userNotFound)
                    enterDataState.networkError -> stringResource(R.string.serverNotWork)
                    enterDataState.aviabitServerTimeOut -> stringResource(R.string.aviabitNotWork)
                    else -> ""
                }
            } else if (enterDataState.codeIsSent) {
                stringResource(R.string.code_sent)
            } else "",
            color = if (enterDataState.codeIsSent) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            },
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(0.9f)
        )

        Button(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp),
            onClick = {}
        ) {
            Text(text = stringResource(R.string.send_code))
        }
    }
}