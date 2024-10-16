package ru.skypaws.features.mainActivity.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.skypaws.data.utils.DateUtils
import ru.skypaws.data.utils.FileStorageService
import ru.skypaws.domain.usecase.download.DownloadLogbookUseCase
import ru.skypaws.domain.usecase.download.IsPathSetUseCase
import ru.skypaws.domain.usecase.download.SaveLogbookToChosenPathUseCase
import ru.skypaws.domain.usecase.download.SaveLogbookUseCase
import ru.skypaws.domain.usecase.settings.GetPathUseCase
import ru.skypaws.domain.usecase.userPayInfo.GetLogbookExpDateUseCase
import ru.skypaws.features.mainActivity.model.LogbookDownloadState
import javax.inject.Inject

sealed class LogbookDownloadIntent {
    data object DownloadLogbook : LogbookDownloadIntent()
    data object SaveLogbookToChosenPath : LogbookDownloadIntent()
    data class SaveLogbook(val uri: Uri) : LogbookDownloadIntent()
}

@HiltViewModel
class LogbookDownloadViewModel @Inject constructor(
    private val getPathUseCase: GetPathUseCase,
    private val getLogbookExpDateUseCase: GetLogbookExpDateUseCase,
    private val dateUtils: DateUtils,
    private val fileServiceStorage: FileStorageService,
    private val downloadLogbookUseCase: DownloadLogbookUseCase,
    private val isPathSetUseCase: IsPathSetUseCase,
    private val saveLogbookUseCase: SaveLogbookUseCase,
    private val saveLogbookToChosenPathUseCase: SaveLogbookToChosenPathUseCase,
) : ViewModel() {
    private val logbookExpDate = getLogbookExpDateUseCase()

    private val _logbookDownloadState =
        MutableStateFlow(LogbookDownloadState(logbookExpDate = dateUtils.getDate(logbookExpDate)))
    val logbookDownloadState: StateFlow<LogbookDownloadState> = _logbookDownloadState.asStateFlow()

    fun handleIntent(intent: LogbookDownloadIntent) {
        when (intent) {
            is LogbookDownloadIntent.DownloadLogbook -> {
                getLogbook()
            }

            // copy temp file to chosen path
            is LogbookDownloadIntent.SaveLogbookToChosenPath -> {
                saveLogbookToChosenPath()
            }

            // copy temp file to path from android file manager
            is LogbookDownloadIntent.SaveLogbook -> {
                saveLogbook(intent.uri)
            }
        }
    }

    /**
     * Sets [logbookDownloadState.LogbookExpDate][_logbookDownloadState] to
     * [logbookExpDate (dd.mm.yyyy)][logbookExpDate]
     * calling [DateUtils.getDate(logbookExpDate)][ru.skypaws.data.utils.DateUtils.getDate], where
     * [logbookExpDate][logbookExpDate] gets from SharedPreferences
     * [UserStorageSharedPrefs.getLogbookExpDate][ru.skypaws.data.storage.UserStorage.getLogbookExpDate].
     *
     * Sets [logbookDownloadState.downloading][_logbookDownloadState] to **true**.
     *
     * Calls [ApiService.downloadLogbook()][ru.skypaws.data.api.ApiService.downloadLogbook]
     * to save file and get file name [contentDispositionFilename][String].
     *
     * Calls [DownloadLogbookRepositoryImpl.isPathSet()]
     * [ru.skypaws.data.repository.DownloadLogbookRepositoryImpl.isPathSet] to check [path][Uri] setting.
     *
     * In case
     * - [path][Uri] is set in settings and correct: [logbookDownloadState][_logbookDownloadState] is set:
     *      - .filename = contentDispositionFilename,
     *      - .downloading = false,
     *      - .downloadedTemp = true,
     *      - .isPathSet = true
     * - [path][Uri] is NOT set or incorrect or does NOT exist in settings:
     * [logbookDownloadState][_logbookDownloadState] is set:
     *      - .filename = contentDispositionFilename,
     *      - .downloading = false,
     *      - .downloadedTemp = true
     * - of [Exception][Exception]: [logbookDownloadState][_logbookDownloadState] is set:
     *      - .downloading = false,
     *      - .downloadedError = true
     */
    private fun getLogbook() {
        _logbookDownloadState.value =
            LogbookDownloadState(
                logbookExpDate = dateUtils.getDate(logbookExpDate),
                downloading = true
            )
        viewModelScope.launch {
            try {
                val contentDispositionFilename = downloadLogbookUseCase()

                if (isPathSetUseCase()) {
                    // path is set in settings and directory exists
                    _logbookDownloadState.value = _logbookDownloadState.value.copy(
                        filename = contentDispositionFilename,
                        downloading = false,
                        downloadedTemp = true,
                        isPathSet = true
                    )
                } else {
                    /*
                        path is not set by user in settings or path is incorrect/does not exists
                        or path is set in settings but directory does not exist
                        (folder is deleted or other error)
                     */
                    _logbookDownloadState.value = _logbookDownloadState.value.copy(
                        filename = contentDispositionFilename,
                        downloading = false,
                        downloadedTemp = true
                    )
                }
            } catch (_: Exception) {
                _logbookDownloadState.value = _logbookDownloadState.value.copy(downloading = false, downloadError = true)
            }
        }
    }

    /**
     * Sets [logbookDownloadState.savingError][_logbookDownloadState] to **false**.
     *
     * By [DownloadLogbookRepositoryImpl.saveLogbook()]
     * [ru.skypaws.data.repository.DownloadLogbookRepositoryImpl.saveLogbook]
     * copies file from temporary URI (**cacheDir**) to the [uri] with
     * [logbookDownloadState.value.filename][logbookDownloadState] name.
     *
     * Sets [logbookDownloadState.logbookExpDate][_logbookDownloadState] to initial state:
     *
     *          LogbookDownloadState(logbookExpDate = dateUtils.getDate(logbookExpDate))
     *
     * In case of [Exception] sets [logbookDownloadState.savingError][_logbookDownloadState] to **true**.
     * @param [uri] URI to save file.
     */
    private fun saveLogbook(uri: Uri) {
        _logbookDownloadState.value = _logbookDownloadState.value.copy(savingError = false)
        viewModelScope.launch {
            try {
                saveLogbookUseCase(uri.toString(), logbookDownloadState.value.filename)

                _logbookDownloadState.value =
                    LogbookDownloadState(logbookExpDate = dateUtils.getDate(logbookExpDate))
            } catch (e: Exception) {
                _logbookDownloadState.value = _logbookDownloadState.value.copy(savingError = true)
            }
        }
    }


    /**
     * Sets [logbookDownloadState.savingError][_logbookDownloadState] to **false**.
     *
     * By [DownloadLogbookRepositoryImpl.saveLogbookToChosenPath()]
     * [ru.skypaws.data.repository.DownloadLogbookRepositoryImpl.saveLogbookToChosenPath]
     * copies file from temporary URI (**cacheDir**) to the
     * [path][ru.skypaws.data.repository.SettingsRepositoryImpl.getPath] from SharedPreferences with
     * [logbookDownloadState.value.filename][logbookDownloadState] name.
     *
     * Sets [logbookDownloadState.logbookExpDate][_logbookDownloadState] to:
     *
     *          _logbookDownloadState.value = LogbookDownloadState(
     *                     logbookExpDate = dateUtils.getDate(logbookExpDate),
     *                     downloaded = true,
     *                     path = fileServiceStorage.transformPath(getPathUseCase())
     *                 )
     *
     * In case of [Exception] sets [logbookDownloadState.savingError][_logbookDownloadState] to **true**.
     */
    private fun saveLogbookToChosenPath() {
        _logbookDownloadState.value = _logbookDownloadState.value.copy(savingError = false)
        viewModelScope.launch {
            try {
                saveLogbookToChosenPathUseCase(logbookDownloadState.value.filename)

                _logbookDownloadState.value = LogbookDownloadState(
                    logbookExpDate = dateUtils.getDate(logbookExpDate),
                    downloaded = true,
                    path = fileServiceStorage.transformPath(getPathUseCase())
                )
            } catch (e: Exception) {
                _logbookDownloadState.value = _logbookDownloadState.value.copy(savingError = true)
            }
        }
    }
}

