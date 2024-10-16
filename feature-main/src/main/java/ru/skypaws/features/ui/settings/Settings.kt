package ru.skypaws.features.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ru.skypaws.presentation.R
import ru.skypaws.features.model.SettingsModel

data class SettingItem(
    val icon: Int,
    val title: Int,
    val listOfOptions: List<Int> = emptyList(),
    val cardTitle: Int = 0
)

@Composable
fun Settings(
    onThemeChange: (Int) -> Unit,
    saveTheme: (Int) -> Unit,
    saveAirportCode:(Int) -> Unit,
    savePath:(String) -> Unit,
    settingsState: SettingsModel,
    content: Context = LocalContext.current
) {

    // list of resources id options (Int) for Airport Code
    val optionsAirportCode = listOf(
        R.string.iata,
        R.string.icao,
        R.string.inside_code
    )
    // list of of resources id options (Int) for theme
    val optionsTheme = listOf(
        R.string.system,
        R.string.light,
        R.string.dark
    )

    // list of setting items
    val listOfItem = listOf(
        // theme
        SettingItem(
            icon = R.drawable.theme_icon,
            title = R.string.theme,
            listOfOptions = optionsTheme,
            cardTitle = R.string.choose_theme
        ),
        // airport code
        SettingItem(
            icon = R.drawable.airport,
            title = R.string.airport_type_to_view,
            listOfOptions = optionsAirportCode,
        ),
        // path to download files
        SettingItem(
            icon = R.drawable.baseline_folder_open_24,
            title = R.string.path_to_download,
        )
    )

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
                savePath(uri.toString())
            }
        }

    SettingsMain(
        onThemeChange = onThemeChange,
        listOfItem = listOfItem,
        changeAirportCode = { newCode -> saveAirportCode(newCode) },
        saveTheme = { newTheme -> saveTheme(newTheme) },
        settingsState = settingsState,
        filePicker = filePicker,
    )
}