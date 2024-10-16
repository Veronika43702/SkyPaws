package ru.skypaws.features.mainActivity.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import ru.skypaws.domain.model.User
import ru.skypaws.features.R
import ru.skypaws.features.mainActivity.model.UpdateModel
import ru.skypaws.features.mainActivity.viewmodel.SettingsViewModel

data class SettingItem(
    val icon: Int,
    val title: Int,
    val listOfOptions: List<Int>,
    val cardTitle: Int
)

@Composable
fun Settings(
    navigateTo: (route: String) -> Unit,
    onThemeChange: (Int) -> Unit,
    user: State<User>,
    clearUser: () -> Unit,
    deleteDB: () -> Unit,
    updateState: UpdateModel,
    installApk: () -> Unit,
    downloadApk: () -> Unit,
) {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val showChoiceMode = remember { mutableStateMapOf<Int, Boolean>() }
    val itemWithCardNeeded = remember { mutableStateOf(SettingItem(0, 0, emptyList(), 0)) }

    // default value for path to download files
    val path by settingsViewModel.path.collectAsState()

    // list of options (Int) for Airport Code
    val optionsAirportCode = listOf(
        R.string.iata,
        R.string.icao,
        R.string.inside_code
    )
    // list of options (Int) for theme
    val optionsTheme = listOf(
        R.string.system,
        R.string.light,
        R.string.dark
    )

    // list of setting items
    val listOfItem = listOf(
        SettingItem(
            icon = R.drawable.theme_icon,
            title = R.string.theme,
            listOfOptions = optionsTheme,
            cardTitle = R.string.choose_theme
        ),
        SettingItem(
            icon = R.drawable.airport,
            title = R.string.airport_type_to_view,
            listOfOptions = optionsAirportCode,
            cardTitle = 0
        ),
        SettingItem(
            icon = R.drawable.baseline_folder_open_24,
            title = R.string.path_to_download,
            listOfOptions = emptyList(),
            cardTitle = 0
        )
    )

    val content = LocalContext.current
    // Picker to choose path to download files
    val filePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
            uri?.let {
                // Request persistable permissions
                val contentResolver = content.contentResolver
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                // Save uri in prefs
                settingsViewModel.savePath(uri.toString())
            }
        }

    SettingsNavMenu(
        navigateTo = navigateTo,
        onThemeChange = onThemeChange,
        listOfItem = listOfItem,
        getTheme = { settingsViewModel.getTheme() },
        getAirportCode = { settingsViewModel.getAirportCode() },
        changeAirportCode = { newCode -> settingsViewModel.changeAirportCode(newCode) },
        saveTheme = { newTheme -> settingsViewModel.saveTheme(newTheme) },
        path = path,
        showChoiceMode = showChoiceMode,
        itemWithCardNeeded = itemWithCardNeeded,
        filePicker = filePicker,
        user = user,
        updateState = updateState,
        clearUser = clearUser,
        deleteDB = deleteDB,
        installApk = installApk,
        downloadApk = downloadApk,
    )
}