package ru.skypaws.features.mainActivity.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.skypaws.data.utils.FileStorageService
import ru.skypaws.domain.usecase.settings.GetNewAirportCodeUseCase
import ru.skypaws.domain.usecase.settings.GetPathUseCase
import ru.skypaws.domain.usecase.settings.GetThemeUseCase
import ru.skypaws.domain.usecase.settings.SaveNewAirportCodeUseCase
import ru.skypaws.domain.usecase.settings.SavePathUseCase
import ru.skypaws.domain.usecase.settings.SaveThemeUseCase
import ru.skypaws.features.R
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getNewAirportCodeUseCase: GetNewAirportCodeUseCase,
    private val saveNewAirportCodeUseCase: SaveNewAirportCodeUseCase,
    private val saveThemeUseCase: SaveThemeUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val savePathUseCase: SavePathUseCase,
    private val getPathUseCase: GetPathUseCase,
    private val fileServiceStorage: FileStorageService
) : ViewModel() {
    // state for path to which files will be downloaded
    private val _path = MutableStateFlow(getPath())
    val path: StateFlow<String?> get() = _path


    /**
     * Gets newAirportCode [SettingsRepositoryImpl.getNewAirportCode()]
     * [ru.skypaws.data.repository.SettingsRepositoryImpl.getNewAirportCode] from SharedPreferences.
     *
     * - 3 - IATA (LED) -> R.string.iata
     * - 5 - ICAO (ULLI) -> R.string.icao
     * - 1 - Inside code (ПЛК) -> R.string.inside_code
     *
     * @return resource string [Int] (R.string.iata, icao or inside_code)
     */
    fun getAirportCode(): Int = when (getNewAirportCodeUseCase()) {
        3 -> R.string.iata
        5 -> R.string.icao
        else -> R.string.inside_code
    }


    /**
     * Saves newAirportCode [SettingsRepositoryImpl.saveNewAirportCode(newCode)]
     * [ru.skypaws.data.repository.SettingsRepositoryImpl.saveNewAirportCode] in SharedPreferences.
     *
     * - 3 - IATA (LED) -> R.string.iata
     * - 5 - ICAO (ULLI) -> R.string.icao
     * - 1 - Inside code (ПЛК) -> R.string.inside_code
     *
     * @param newCode [Int] (1, 3, 5)
     */
    fun changeAirportCode(newCode: String) {
        val code = when (newCode.toInt()) {
            R.string.iata -> 3
            R.string.icao -> 5
            else -> 1
        }
        saveNewAirportCodeUseCase(code)
    }


    /**
     * Saves newAirportCode [SettingsRepositoryImpl.saveTheme(newTheme)]
     * [ru.skypaws.data.repository.SettingsRepositoryImpl.saveTheme] in SharedPreferences.
     *
     * - 1 - light -> R.string.light
     * - 2 - dark -> R.string.dark
     * - 0 - system -> R.string.system
     *
     * @param newTheme [Int] (0, 1, 2)
     */
    fun saveTheme(newTheme: Int) {
        val theme = when (newTheme) {
            R.string.system -> 0
            R.string.light -> 1
            else -> 2
        }

        saveThemeUseCase(theme)
    }

    /**
     * Gets theme [SettingsRepositoryImpl.getTheme()]
     * [ru.skypaws.data.repository.SettingsRepositoryImpl.getTheme] from SharedPreferences.
     *
     * - 1 - light -> R.string.light
     * - 2 - dark -> R.string.dark
     * - 0 - system -> R.string.system
     *
     * @return resource string [Int] (R.string.light, dark or system)
     */
    fun getTheme(): Int {
        val theme = getThemeUseCase()
        return when (theme) {
            1 -> R.string.light
            2 -> R.string.dark
            else -> R.string.system
        }
    }


    /**
     * Saves path to download files [SettingsRepositoryImpl.savePath(uri)]
     * [ru.skypaws.data.repository.SettingsRepositoryImpl.savePath] from SharedPreferences.
     * @param uri [String]
     */
    fun savePath(uri: String) {
        savePathUseCase(uri)
        // change path for UI
        _path.value = transformPath(uri)
    }

    /**
     * Gets path to download files [SettingsRepositoryImpl.getPath()]
     * [ru.skypaws.data.repository.SettingsRepositoryImpl.getPath] from SharedPreferences.
     * @return uri [String]
     */
    private fun getPath(): String? = transformPath(getPathUseCase())

    /**
     * Transforms path to human view [FileStorageService.transformPath(uri)]
     * [ru.skypaws.data.utils.FileStorageService.transformPath].
     * @param uri [String]
     * @return path [String]
     */
    private fun transformPath(uri: String?): String? = fileServiceStorage.transformPath(uri)
}