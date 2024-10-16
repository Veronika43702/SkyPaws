package ru.skypaws.features.enterActivity.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import ru.skypaws.presentation.R
import ru.skypaws.presentation.model.UpdateModel
import ru.skypaws.presentation.ui.theme.LocalCustomColors

@Composable
internal fun UsernameAndPasswordFields(
    modifier: Modifier = Modifier,
    updateState: UpdateModel = UpdateModel(),
    username: MutableState<String>,
    password: MutableState<String>,
    passwordVisible: MutableState<Boolean>,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        OutlinedTextField(
            enabled = !updateState.newDB,
            value = username.value,
            modifier = Modifier.fillMaxWidth(0.95f),
            onValueChange = { username.value = it.trim() },
            label = { Text(stringResource(R.string.username)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = LocalCustomColors.current.blackWhite,
                unfocusedTextColor = LocalCustomColors.current.blackWhite,
            )
        )

        OutlinedTextField(
            enabled = !updateState.newDB,
            value = password.value,
            onValueChange = { password.value = it.trim() },
            modifier = Modifier.fillMaxWidth(0.95f),
            label = { Text(stringResource(R.string.password)) },
            singleLine = true,
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible.value)
                    painterResource(R.drawable.baseline_visibility_24)
                else painterResource(R.drawable.baseline_visibility_off_24)

                // Please provide localized description for accessibility services
                val description =
                    if (passwordVisible.value) "Hide password" else "Show password"

                IconButton(onClick = {
                    passwordVisible.value = !passwordVisible.value
                }) {
                    Icon(painter = image, description)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = LocalCustomColors.current.blackWhite,
                unfocusedTextColor = LocalCustomColors.current.blackWhite,
            )
        )
    }
}