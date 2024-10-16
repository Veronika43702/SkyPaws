package ru.skypaws.features.enterActivity.ui.enterwithcode

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.skypaws.presentation.R
import ru.skypaws.features.enterActivity.ui.common.Boxes
import ru.skypaws.features.enterActivity.ui.common.CodeButton
import ru.skypaws.features.enterActivity.ui.common.CodeButtonPreview
import ru.skypaws.features.enterActivity.ui.common.SignInUpButton
import ru.skypaws.features.enterActivity.ui.common.UsernameAndPasswordFields
import ru.skypaws.features.enterActivity.model.EnterModel
import ru.skypaws.presentation.model.LoadingDataState
import ru.skypaws.presentation.model.UpdateModel
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@Composable
fun EnterWithCodeBox(
    enterDataState: EnterModel,
    loadingState: LoadingDataState,
    username: MutableState<String>,
    password: MutableState<String>,
    passwordVisible: MutableState<Boolean>,
    code: MutableState<String>,
    register: () -> Unit,
    getResponseForCode: () -> Unit,
    snackBarHostState: SnackbarHostState,
) {
    Boxes(
        loadingState = loadingState,
        enterDataState = enterDataState,
        snackBarHostState = snackBarHostState,
        updateCard = false
    ) { modifierTextFields, _ ->
        // text fields (username & password)
        UsernameAndPasswordFields(
            username = username,
            password = password,
            passwordVisible = passwordVisible,
            modifier = modifierTextFields
        )

        // error text and button for Code
        CodeButton(
            enterDataState = enterDataState,
            getResponseForCode = getResponseForCode,
        )


        // code field
        OutlinedTextField(
            value = code.value,
            enabled = enterDataState.codeIsSent,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(bottom = 12.dp),
            onValueChange = { code.value = it.trim() },
            label = { Text(stringResource(R.string.code)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = LocalCustomColors.current.blackWhite,
                unfocusedTextColor = LocalCustomColors.current.blackWhite,
                disabledTextColor = LocalCustomColors.current.blackWhite,
                disabledLabelColor = LocalCustomColors.current.blackWhite.copy(alpha = 0.5f),
            )
        )

        // Button for Enter With Code
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Enter button with error text
            SignInUpButton(enterDataState) {
                Button(
                    enabled = enterDataState.codeIsSent && code.value != "",
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
                    onClick = { register() }
                ) {
                    Text(text = stringResource(R.string.enter_with_code))
                }
            }
        }
    }
}

@Preview(
    name = "dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "ru"
)
@Preview(
    showBackground = true,
    name = "light",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "ru"
)
@Composable
fun EnterWithCodeBoxPreview(
    enterDataState: EnterModel = EnterModel(
        networkError = true,
        registerError = true,
        codeSendingError = true,
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
            loadingState = loadingState,
            enterDataState = enterDataState,
            snackBarHostState = snackBarHostState,
        ) { modifierTextFields, _ ->
            // text fields (username & password)
            UsernameAndPasswordFields(
                username = username,
                password = password,
                passwordVisible = passwordVisible,
                modifier = modifierTextFields
            )

            // error text and button for Code
            CodeButtonPreview(
                enterDataState,
            )

            // error text and button for Code
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CodeButtonPreview(
                    enterDataState,
                )
            }

            // code field
            OutlinedTextField(
                value = code.value,
                enabled = enterDataState.codeIsSent,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(bottom = 12.dp),
                onValueChange = { code.value = it.trim() },
                label = { Text(stringResource(R.string.code)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = LocalCustomColors.current.blackWhite,
                    unfocusedTextColor = LocalCustomColors.current.blackWhite,
                    disabledTextColor = LocalCustomColors.current.blackWhite,
                    disabledLabelColor = LocalCustomColors.current.blackWhite.copy(alpha = 0.5f),
                )
            )

            // Button for Enter With Code
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Enter button with error text
                SignInUpButton(enterDataState) {
                    Button(
                        enabled = enterDataState.codeIsSent && code.value != "",
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
                        onClick = {

                        }
                    ) {
                        Text(text = stringResource(R.string.enter_with_code))
                    }
                }
            }
        }
    }
}