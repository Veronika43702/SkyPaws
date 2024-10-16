package ru.skypaws.features.enterActivity.ui.enter

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.skypaws.presentation.R
import ru.skypaws.features.enterActivity.model.EnterModel
import ru.skypaws.features.enterActivity.ui.common.Boxes
import ru.skypaws.features.enterActivity.ui.common.SignInUpButton
import ru.skypaws.features.enterActivity.ui.common.UsernameAndPasswordFields
import ru.skypaws.presentation.model.UpdateModel
import ru.skypaws.presentation.model.LoadingDataState
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@Composable
fun EnterBox(
    loadingState: LoadingDataState,
    updateState: UpdateModel,
    enterDataState: EnterModel,
    username: MutableState<String>,
    password: MutableState<String>,
    passwordVisible: MutableState<Boolean>,
    downloadApk: () -> Unit,
    signIn: () -> Unit,
    snackBarHostState: SnackbarHostState,
    onEnterWithCode: () -> Unit,
) {
    Boxes(
        updateState = updateState,
        loadingState = loadingState,
        enterDataState = enterDataState,
        snackBarHostState = snackBarHostState,
        updateCard = true,
        downloadApk = downloadApk
    ) { modifierTextFields, modifierColumn ->
        // text fields (username & password)
        UsernameAndPasswordFields(
            username = username,
            password = password,
            passwordVisible = passwordVisible,
            updateState = updateState,
            modifier = modifierTextFields
        )

        // Buttons
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifierColumn
        ) {
            // Enter button with error text
            SignInUpButton(enterDataState) {
                Button(
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                    enabled = (username.value != "" && password.value != "" && !updateState.newDB),
                    onClick = { signIn() }
                ) {
                    Text(text = stringResource(R.string.enter))
                }
            }

            Button(
                enabled = !updateState.newDB,
                modifier = Modifier.padding(bottom = 8.dp),
                onClick = {
                    onEnterWithCode()
                }
            ) {
                Text(text = stringResource(R.string.enter_with_code))
            }
        }
    }
}

@Preview(
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
fun EnterPreview(
    enterDataState: EnterModel = EnterModel(
        signInError = true,
        wrongData = true,
    ),
    username: MutableState<String> = mutableStateOf(""),
    password: MutableState<String> = mutableStateOf(""),
    code: MutableState<String> = mutableStateOf(""),
    passwordVisible: MutableState<Boolean> = mutableStateOf(false),
    loadingState: LoadingDataState = LoadingDataState(loadingAviabitData = false),
    updateState: UpdateModel = UpdateModel(newDB = false),
    snackBarHostState: SnackbarHostState = SnackbarHostState(),
) {
    SkyPawsTheme {
        Boxes(
            updateState = updateState,
            loadingState = loadingState,
            enterDataState = enterDataState,
            snackBarHostState = snackBarHostState,
            updateCard = true
        ) { modifierTextFields, modifierColumn ->
            // text fields (username & password)
            UsernameAndPasswordFields(
                username = username,
                password = password,
                passwordVisible = passwordVisible,
                updateState = updateState,
                modifier = modifierTextFields
            )

            // Buttons
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifierColumn
            ) {
                // Enter button with error text
                SignInUpButton(enterDataState) {
                    Button(
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                        enabled = (username.value != "" && password.value != "" && !updateState.newDB),
                        onClick = { }
                    ) {
                        Text(text = stringResource(R.string.enter))
                    }
                }

                Button(
                    enabled = !updateState.newDB,
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = {}
                ) {
                    Text(text = stringResource(R.string.enter_with_code))
                }
            }
        }
    }
}