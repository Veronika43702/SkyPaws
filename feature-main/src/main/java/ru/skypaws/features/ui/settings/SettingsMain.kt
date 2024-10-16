package ru.skypaws.features.ui.settings

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.skypaws.mobileapp.data.utils.dbVersion
import ru.skypaws.mobileapp.data.utils.version
import ru.skypaws.presentation.R
import ru.skypaws.features.model.SettingsModel
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@Composable
fun SettingsMain(
    onThemeChange: (Int) -> Unit,
    listOfItem: List<SettingItem>,

    changeAirportCode: (Int) -> Unit,
    saveTheme: (Int) -> Unit,

    settingsState: SettingsModel,
    showChoiceMode: SnapshotStateMap<Int, Boolean> = remember { mutableStateMapOf() },
    itemWithCardNeeded: MutableState<SettingItem> = remember { mutableStateOf(SettingItem(0, 0)) },
    filePicker: ManagedActivityResultLauncher<Uri?, Uri?>,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            listOfItem.forEach { item ->
                // option chosen now. Default as saved locally.
                var selectedOption by remember {
                    mutableStateOf(
                        when (item.title) {
                            R.string.theme -> settingsState.theme // (0, 1, 2)
                            R.string.airport_type_to_view -> settingsState.airportCode // (1, 3, 5)
                            else -> null
                        }
                    )
                }

                SettingItem(
                    item = item,
                    selectedOption = selectedOption,
                    selectedPath = settingsState.path, // for "path to download" item.
                    onClick = {
                        // change visibility of choice composable
                        showChoiceMode[item.title] = !(showChoiceMode[item.title] ?: false)
                    }
                )

                // if item is clicked to expand/get setting card, etc.
                if (showChoiceMode[item.title] == true) {
                    when (item.title) {
                        // for Airport Code setting -> expanded Radio Button Group
                        R.string.airport_type_to_view -> {
                            Column {
                                item.listOfOptions.forEach { option ->
                                    RadioButtonGroup(
                                        optionResourceId = option,
                                        optionValue = when (option) {
                                            R.string.iata -> 3
                                            R.string.icao -> 5
                                            else -> 1
                                        },
                                        selectedOption = selectedOption,
                                        onOptionSelected = { newOption ->
                                            selectedOption = newOption
                                            changeAirportCode(newOption)
                                        }
                                    )
                                }
                            }
                        }

                        // for Theme setting -> show Card with Radio Button Group
                        R.string.theme -> {
                            itemWithCardNeeded.value = item
                        }

                        // for Path setting -> launch Android FilePicker
                        R.string.path_to_download -> {
                            filePicker.launch(null)
                            showChoiceMode[item.title] = false
                        }
                    }
                } else {
                    when (item.title) {
                        R.string.theme -> selectedOption = settingsState.theme
                        R.string.path_to_download -> selectedOption = null
                    }
                }
            }
        }

        // for settings, which need to show card
        if (showChoiceMode[itemWithCardNeeded.value.title] == true) {
            // chosen value (default at the beginning)
            var selectedOptionForCard by remember {
                mutableIntStateOf(
                    when (itemWithCardNeeded.value.title) {
                        R.string.theme -> settingsState.theme // (0, 1, 2)
                        else -> -1
                    }
                )
            }


            MyCard(
                title = itemWithCardNeeded.value.cardTitle,
                onCancel = {
                    // set theme from local storage for SkyPawTheme in MainActivity
                    onThemeChange(-1)
                    showChoiceMode[itemWithCardNeeded.value.title] = false
                },
                onConfirm = {
                    // save theme locally
                    saveTheme(selectedOptionForCard)
                    // set theme from local storage for SkyPawTheme in MainActivity
                    onThemeChange(-1)
                    showChoiceMode[itemWithCardNeeded.value.title] = false
                }
            ) {
                itemWithCardNeeded.value.listOfOptions.forEach { option ->
                    RadioButtonGroup(
                        optionResourceId = option,
                        optionValue = when (option) {
                            R.string.light -> 1
                            R.string.dark -> 2
                            else -> 0
                        },
                        selectedOption = selectedOptionForCard,
                        onOptionSelected = { newOption ->
                            selectedOptionForCard = newOption
                            onThemeChange(newOption)
                        }
                    )
                }
            }
        }

        // app version number at the bottom
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            text = stringResource(id = R.string.version, "$version, $dbVersion"),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
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
fun SettingsMainPreview(
    settingsState: SettingsModel = SettingsModel(
        theme = 1,
        airportCode = 1,
        path = "path"
    ),
    listOfItem: List<SettingItem> = listOf(
        SettingItem(
            icon = R.drawable.theme_icon,
            title = R.string.theme,
            listOfOptions = listOf(
                R.string.system,
                R.string.light,
                R.string.dark
            ),
            cardTitle = R.string.choose_theme
        ),
        SettingItem(
            icon = R.drawable.airport,
            title = R.string.airport_type_to_view,
            listOfOptions = listOf(
                R.string.iata,
                R.string.icao,
                R.string.inside_code
            ),
        ),
        SettingItem(
            icon = R.drawable.baseline_folder_open_24,
            title = R.string.path_to_download,
        )
    ),
    path: String? = "Путь для сохранения",
    showChoiceMode: SnapshotStateMap<Int, Boolean> = remember {
        // change to "theme" or "airport_type_to_view" and true/false for expanded mode
        mutableStateMapOf(R.string.airport_type_to_view to true)
    },
    itemWithCardNeeded: MutableState<SettingItem> = remember {
        mutableStateOf(
            SettingItem(
                0,
                0,
            )
        )
    },
) {
    SkyPawsTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                listOfItem.forEach { item ->
                    // option chosen now. Default as saved locally.
                    var selectedOption by remember {
                        mutableStateOf(
                            when (item.title) {
                                R.string.theme -> settingsState.theme // (0, 1, 2)
                                R.string.airport_type_to_view -> settingsState.airportCode // (1, 3, 5)
                                else -> null
                            }
                        )
                    }

                    SettingItem(
                        item = item,
                        selectedOption = selectedOption,
                        selectedPath = settingsState.path, // for "path to download" item.
                        onClick = {
                            // change visibility of choice composable
                            showChoiceMode[item.title] = !(showChoiceMode[item.title] ?: false)
                        }
                    )

                    // if item is clicked to expand/get setting card, etc.
                    if (showChoiceMode[item.title] == true) {
                        when (item.title) {
                            // for Airport Code setting -> expanded Radio Button Group
                            R.string.airport_type_to_view -> {
                                Column {
                                    item.listOfOptions.forEach { option ->
                                        RadioButtonGroup(
                                            optionResourceId = option,
                                            optionValue = when (option) {
                                                R.string.iata -> 3
                                                R.string.icao -> 5
                                                else -> 1
                                            },
                                            selectedOption = selectedOption,
                                            onOptionSelected = { newOption ->
                                                selectedOption = newOption
                                            }
                                        )
                                    }
                                }
                            }

                            // for Theme setting -> show Card with Radio Button Group
                            R.string.theme -> {
                                itemWithCardNeeded.value = item
                            }

                            // for Path setting -> launch Android FilePicker
                            R.string.path_to_download -> {
                                showChoiceMode[item.title] = false
                            }
                        }
                    } else {
                        when (item.title) {
                            R.string.theme -> selectedOption = settingsState.theme
                            R.string.path_to_download -> selectedOption = null
                        }
                    }
                }
            }

            // for settings, which need to show card
            if (showChoiceMode[itemWithCardNeeded.value.title] == true) {
                // chosen value (default at the beginning)
                var selectedOptionForCard by remember {
                    mutableIntStateOf(
                        when (itemWithCardNeeded.value.title) {
                            R.string.theme -> settingsState.theme // (0, 1, 2)
                            else -> -1
                        }
                    )
                }


                MyCard(
                    title = itemWithCardNeeded.value.cardTitle,
                    onCancel = {
                        // set theme from local storage for SkyPawTheme in MainActivity
                        showChoiceMode[itemWithCardNeeded.value.title] = false
                    },
                    onConfirm = {
                        showChoiceMode[itemWithCardNeeded.value.title] = false
                    }
                ) {
                    itemWithCardNeeded.value.listOfOptions.forEach { option ->
                        RadioButtonGroup(
                            optionResourceId = option,
                            optionValue = when (option) {
                                R.string.light -> 1
                                R.string.dark -> 2
                                else -> 0
                            },
                            selectedOption = selectedOptionForCard,
                            onOptionSelected = { newOption ->
                                selectedOptionForCard = newOption
                            }
                        )
                    }
                }
            }

            // app version number at the bottom
            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                text = stringResource(id = R.string.version, "$version, $dbVersion"),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}