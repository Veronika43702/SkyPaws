package ru.skypaws.features.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.data.utils.FileStorageService
import ru.skypaws.mobileapp.domain.usecase.settings.airportCode.GetNewAirportCodeFlowUseCase
import ru.skypaws.mobileapp.domain.usecase.settings.path.GetPathFlowUseCase
import ru.skypaws.mobileapp.domain.usecase.settings.theme.GetThemeFlowUseCase
import ru.skypaws.mobileapp.domain.usecase.settings.airportCode.SaveNewAirportCodeUseCase
import ru.skypaws.mobileapp.domain.usecase.settings.path.SavePathUseCase
import ru.skypaws.mobileapp.domain.usecase.settings.theme.SaveThemeUseCase
import ru.skypaws.features.model.SettingsModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getNewAirportCodeFlowUseCase: GetNewAirportCodeFlowUseCase,
    private val saveNewAirportCodeUseCase: SaveNewAirportCodeUseCase,
    private val saveThemeUseCase: SaveThemeUseCase,
    private val getThemeFlowUseCase: GetThemeFlowUseCase,
    private val savePathUseCase: SavePathUseCase,
    private val getPathFlowUseCase: GetPathFlowUseCase,
    private val fileServiceStorage: FileStorageService
) : ViewModel() {
    private val _settingState = MutableStateFlow(ru.skypaws.features.model.SettingsModel())
    val settingState: StateFlow<ru.skypaws.features.model.SettingsModel> get() = _settingState

    /**
     * Gets theme, airport code and path settings from local storage.
     */
    fun observeSettings() {
        viewModelScope.launch {
            combine(
                getThemeFlowUseCase(),
                getNewAirportCodeFlowUseCase(),
                getPathFlowUseCase()
            ) { theme, airportCode, path ->
                Triple(theme, airportCode, path)
            }.collect { (theme, airportCode, path) ->
                _settingState.update {
                    it.copy(
                        theme = theme,
                        airportCode = airportCode,
                        path = transformPath(path)
                    )
                }
            }
        }
    }

    /**
     * Saves newAirportCode [AirportCodeSettingRepository.saveNewAirportCode(newCode)]
     * [ru.skypaws.mobileapp.repository.settings.AirportCodeSettingRepository.saveNewAirportCode] locally.
     *
     * - 3 - IATA (LED) -> iata
     * - 5 - ICAO (ULLI) -> icao
     * - 1 - Inside code (ПЛК) -> inside_code
     *
     * @param newCode [Int] (1, 3, 5)
     */
    fun saveAirportCode(newCode: Int) {
        viewModelScope.launch {
            saveNewAirportCodeUseCase(newCode)
        }
    }

    /**
     * Saves theme [ThemeSettingRepository.saveTheme(newTheme)]
     * [ru.skypaws.mobileapp.repository.settings.ThemeSettingRepository.saveTheme] locally.
     *
     * - 1 - light
     * - 2 - dark
     * - 0 - system
     *
     * @param newTheme [Int] (0, 1, 2)
     */
    fun saveTheme(newTheme: Int) {
        viewModelScope.launch {
            saveThemeUseCase(newTheme)
        }
    }

    /**
     * Saves path to download files [PathSettingRepository.savePath.savePath(uri: String)]
     * [ru.skypaws.mobileapp.repository.settings.PathSettingRepository.savePath] from SharedPreferences.
     * @param uri [String]
     */
    fun savePath(uri: String) {
        viewModelScope.launch {
            savePathUseCase(uri)
            // change path for UI
            _settingState.update { it.copy(path = transformPath(uri)) }
        }
    }

    /**
     * Transforms path to human view [FileStorageService.transformPath(uri)]
     * [ru.skypaws.mobileapp.utils.FileStorageService.transformPath].
     * @param uri [String]
     * @return path [String]
     */
    private fun transformPath(uri: String?): String? = fileServiceStorage.transformPath(uri)
}