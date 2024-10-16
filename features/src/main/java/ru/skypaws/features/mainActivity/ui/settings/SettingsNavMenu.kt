package ru.skypaws.features.mainActivity.ui.settings

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
import androidx.compose.runtime.State
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
import ru.skypaws.data.utils.dbVersion
import ru.skypaws.data.utils.version
import ru.skypaws.domain.model.User
import ru.skypaws.features.R
import ru.skypaws.features.mainActivity.model.UpdateModel
import ru.skypaws.features.mainActivity.ui.topBarWithNavMenu.NavMenu
import ru.skypaws.features.ui.theme.SkyPawsTheme

@Composable
fun SettingsNavMenu(
    navigateTo: (route: String) -> Unit,
    onThemeChange: (Int) -> Unit,
    listOfItem: List<SettingItem>,

    getTheme: () -> Int,
    getAirportCode: () -> Int,
    changeAirportCode: (String) -> Unit,
    saveTheme: (Int) -> Unit,

    path: String?,
    showChoiceMode: SnapshotStateMap<Int, Boolean>,
    itemWithCardNeeded: MutableState<SettingItem>,
    filePicker: ManagedActivityResultLauncher<Uri?, Uri?>,

    user: State<User>,
    clearUser: () -> Unit,
    deleteDB: () -> Unit,
    updateState: UpdateModel,
    installApk: () -> Unit,
    downloadApk: () -> Unit,
) {
    NavMenu(
        title = stringResource(id = R.string.settings),
        isActionNeed = false,
        navigateTo = navigateTo,
        user = user,
        updateState = updateState,
        clearUser = clearUser,
        deleteDB = deleteDB,
        installApk = installApk,
        downloadApk = downloadApk,
    )
    { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                listOfItem.forEachIndexed { _, item ->
                    // default (current) options
                    // for file path -> viewModel state called "path" earlier
                    val code = getAirportCode().toString()
                    val theme = getTheme().toString()

                    // option chosen now
                    var selectedOption by remember {
                        mutableStateOf(
                            when (item.title) {
                                R.string.theme -> theme
                                R.string.airport_type_to_view -> code
                                R.string.path_to_download -> path
                                else -> null
                            }
                        )
                    }

                    SettingItem(
                        item = item,
                        selectedOption = selectedOption,
                        onClick = {
                            showChoiceMode[item.title] = !(showChoiceMode[item.title] ?: false)
                        }
                    )

                    // if item is clicked to expand/get setting card, etc.
                    if (showChoiceMode[item.title] == true) {
                        when (item.title) {
                            // for Airport Code setting -> Radio Button Group
                            R.string.airport_type_to_view -> {
                                selectedOption = code
                                Column {
                                    item.listOfOptions.forEach { option ->
                                        RadioButtonGroup(
                                            optionId = option.toString(),
                                            selectedOption = selectedOption,
                                            onOptionSelected = {
                                                selectedOption = it
                                                changeAirportCode(it)
                                            }
                                        )
                                    }
                                }
                            }

                            // for Theme setting -> show Card
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
                            R.string.theme -> {
                                selectedOption = getTheme().toString()
                            }

                            R.string.path_to_download -> {
                                selectedOption = path
                            }
                        }
                    }
                }
            }

            // for settings, which need to show card
            if (showChoiceMode[itemWithCardNeeded.value.title] == true) {
                // default value
                val theme = getTheme()

                // chosen value (default at the beginning)
                var selectedOptionForCard by remember {
                    mutableIntStateOf(
                        when (itemWithCardNeeded.value.title) {
                            R.string.theme -> theme
                            else -> -1
                        }
                    )
                }


                MyCard(
                    title = itemWithCardNeeded.value.cardTitle,
                    onCancel = {
                        showChoiceMode[itemWithCardNeeded.value.title] = false
                        // set theme from prefs for SkyPawTheme in MainActivity
                        onThemeChange(-1)
                    },
                    onConfirm = {
                        showChoiceMode[itemWithCardNeeded.value.title] = false
                        // theme changing in prefs
                        saveTheme(selectedOptionForCard)
                        // set theme from prefs for SkyPawTheme in MainActivity
                        onThemeChange(-1)
                    }
                ) {
                    itemWithCardNeeded.value.listOfOptions.forEach { option ->
                        RadioButtonGroup(
                            optionId = option.toString(),
                            selectedOption = selectedOptionForCard.toString(),
                            onOptionSelected = {
                                selectedOptionForCard = it.toInt()
                                // only for theme choosing (for other setting in future -> add condition for title)
                                when (option) {
                                    itemWithCardNeeded.value.listOfOptions[0] -> onThemeChange(0)
                                    itemWithCardNeeded.value.listOfOptions[1] -> onThemeChange(1)
                                    itemWithCardNeeded.value.listOfOptions[2] -> onThemeChange(2)
                                }
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
fun SettingsNavMenuPreview(
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
            cardTitle = 0
        ),
        SettingItem(
            icon = R.drawable.baseline_folder_open_24,
            title = R.string.path_to_download,
            listOfOptions = emptyList(),
            cardTitle = 0
        )
    ),
    path: String? = "Путь для сохранения",
    showChoiceMode: SnapshotStateMap<Int, Boolean> = remember {
        // change to "theme" or "airport_type_to_view" and true/false for expanded mode
        mutableStateMapOf(R.string.theme to true)
    },
    itemWithCardNeeded: MutableState<SettingItem> = remember {
        mutableStateOf(
            SettingItem(
                0,
                0,
                emptyList(),
                0
            )
        )
    },
) {
    val chosenTheme = R.string.dark.toString()
    SkyPawsTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                listOfItem.forEachIndexed { _, item ->
                    // default (current) options
                    // for file path -> viewModel state called "path" earlier
                    val code = R.string.iata.toString()
                    val theme = R.string.dark.toString()

                    // option chosen now
                    var selectedOption by remember {
                        mutableStateOf(
                            when (item.title) {
                                R.string.theme -> theme
                                R.string.airport_type_to_view -> code
                                R.string.path_to_download -> path
                                else -> null
                            }
                        )
                    }

                    SettingItemPreview(
                        item = item,
                        selectedOption = selectedOption,
                    )

                    // if item is clicked to expand/get setting card, etc.
                    if (showChoiceMode[item.title] == true) {
                        when (item.title) {
                            // for Airport Code setting -> Radio Button Group
                            R.string.airport_type_to_view -> {
                                selectedOption = code
                                Column {
                                    item.listOfOptions.forEach { option ->
                                        RadioButtonGroup(
                                            optionId = option.toString(),
                                            selectedOption = selectedOption,
                                            onOptionSelected = {
                                                selectedOption = it
                                            }
                                        )
                                    }
                                }
                            }

                            // for Theme setting -> show Card
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
                            R.string.theme -> {
                                selectedOption = chosenTheme
                            }

                            R.string.path_to_download -> {
                                selectedOption = path
                            }
                        }
                    }
                }
            }

            // for settings, which need to show card
            if (showChoiceMode[itemWithCardNeeded.value.title] == true) {
                // default value
                val theme = chosenTheme.toInt()

                // chosen value (default at the beginning)
                val selectedOptionForCard = remember {
                    mutableIntStateOf(
                        when (itemWithCardNeeded.value.title) {
                            R.string.theme -> theme
                            else -> -1
                        }
                    )
                }


                MyCard(
                    title = itemWithCardNeeded.value.cardTitle,
                    onCancel = {
                        showChoiceMode[itemWithCardNeeded.value.title] = false
                        // set theme from prefs for SkyPawTheme in MainActivity
                    },
                    onConfirm = {
                        showChoiceMode[itemWithCardNeeded.value.title] = false

                    }
                ) {
                    itemWithCardNeeded.value.listOfOptions.forEach { option ->
                        RadioButtonGroupPreview(
                            optionId = option.toString(),
                            selectedOption = selectedOptionForCard.toString(),
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