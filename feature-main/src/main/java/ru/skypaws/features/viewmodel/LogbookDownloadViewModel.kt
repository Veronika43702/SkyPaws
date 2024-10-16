package ru.skypaws.features.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.data.utils.DateUtils
import ru.skypaws.mobileapp.data.utils.FileStorageService
import ru.skypaws.mobileapp.domain.usecase.download.DownloadLogbookUseCase
import ru.skypaws.mobileapp.domain.usecase.download.IsPathSetUseCase
import ru.skypaws.mobileapp.domain.usecase.download.SaveLogbookToChosenPathUseCase
import ru.skypaws.mobileapp.domain.usecase.download.SaveLogbookUseCase
import ru.skypaws.mobileapp.domain.usecase.settings.path.GetPathUseCase
import ru.skypaws.mobileapp.domain.usecase.userPayInfo.GetLogbookExpDateUseCase
import ru.skypaws.features.model.LogbookDownloadState
import javax.inject.Inject

sealed class LogbookDownloadIntent {
    data object GetLogbookExpData : LogbookDownloadIntent()
    data object DownloadLogbook : LogbookDownloadIntent()
    data object SaveLogbookToChosenPath : LogbookDownloadIntent()
    data class SaveLogbook(val uri: Uri) : LogbookDownloadIntent()
}

@HiltViewModel
class LogbookDownloadViewModel @Inject constructor(
    private val getPathUseCase: GetPathUseCase,
    private val getLogbookExpDateUseCase: GetLogbookExpDateUseCase,
    private val fileServiceStorage: FileStorageService,
    private val downloadLogbookUseCase: DownloadLogbookUseCase,
    private val isPathSetUseCase: IsPathSetUseCase,
    private val saveLogbookUseCase: SaveLogbookUseCase,
    private val saveLogbookToChosenPathUseCase: SaveLogbookToChosenPathUseCase,
) : ViewModel() {
    private val _logbookDownloadState = MutableStateFlow(ru.skypaws.features.model.LogbookDownloadState())
    val logbookDownloadState: StateFlow<ru.skypaws.features.model.LogbookDownloadState> = _logbookDownloadState.asStateFlow()

    fun handleIntent(intent: LogbookDownloadIntent) {
        when (intent) {
            is LogbookDownloadIntent.GetLogbookExpData -> {
                getLogbookData()
            }

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

    private fun getLogbookData() {
        viewModelScope.launch {
            _logbookDownloadState.update {
                it.copy(logbookExpDate = DateUtils.getDate(getLogbookExpDateUseCase()))
            }
        }
    }

    /**
     * Sets [logbookDownloadState.LogbookExpDate][_logbookDownloadState] to (dd.mm.yyyy) value
     * from local storage by
     * [UserPayInfoLocalDataSource.getLogbookExpDate][ru.skypaws.mobileapp.datasource.local.user.UserPayInfoLocalDataSource.getLogbookExpDate].
     *
     * Sets [logbookDownloadState.downloading][_logbookDownloadState] to **true**.
     *
     * Calls [ApiService.downloadLogbook()][ru.skypaws.mobileapp.datasource.remote.api.ApiService.downloadLogbook]
     * to save file and get file name [contentDispositionFilename][String].
     *
     * Calls [DownloadLogbookRepositoryImpl.isPathSet()]
     * [ru.skypaws.mobileapp.repository.DownloadLogbookRepositoryImpl.isPathSet] to check [path][Uri] setting.
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
     *  @see [DateUtils.getDate]
     */
    private fun getLogbook() {
        viewModelScope.launch {

            _logbookDownloadState.update {
                ru.skypaws.features.model.LogbookDownloadState(
                    logbookExpDate = DateUtils.getDate(getLogbookExpDateUseCase()),
                    downloading = true
                )
            }

            try {
                val contentDispositionFilename = downloadLogbookUseCase()

                if (isPathSetUseCase()) {
                    // path is set in settings and directory exists
                    _logbookDownloadState.update {
                        it.copy(
                            filename = contentDispositionFilename,
                            downloading = false,
                            downloadedTemp = true,
                            isPathSet = true
                        )
                    }
                } else {
                    /*
                        path is not set by user in settings or path is incorrect/does not exists
                        or path is set in settings but directory does not exist
                        (folder is deleted or other error)
                     */
                    _logbookDownloadState.update {
                        it.copy(
                            filename = contentDispositionFilename,
                            downloading = false,
                            downloadedTemp = true
                        )
                    }
                }
            } catch (_: Exception) {
                _logbookDownloadState.update { it.copy(downloading = false, downloadError = true) }
            }
        }
    }

    /**
     * Sets [logbookDownloadState.savingError][_logbookDownloadState] to **false**.
     *
     * By [DownloadLogbookRepositoryImpl.saveLogbook()]
     * [ru.skypaws.mobileapp.repository.DownloadLogbookRepositoryImpl.saveLogbook]
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
        _logbookDownloadState.update { it.copy(savingError = false) }
        viewModelScope.launch {
            try {
                saveLogbookUseCase(uri.toString(), logbookDownloadState.value.filename)

                _logbookDownloadState.update {
                    ru.skypaws.features.model.LogbookDownloadState(logbookExpDate = it.logbookExpDate)
                }
            } catch (e: Exception) {
                _logbookDownloadState.update { it.copy(savingError = true) }
            }
        }
    }


    /**
     * Sets [logbookDownloadState.savingError][_logbookDownloadState] to **false**.
     *
     * By [DownloadLogbookRepositoryImpl.saveLogbookToChosenPath()]
     * [ru.skypaws.mobileapp.repository.DownloadLogbookRepositoryImpl.saveLogbookToChosenPath]
     * copies file from temporary URI (**cacheDir**) to the
     * [path][ru.skypaws.mobileapp.repository.settings.PathSettingRepository.getPath] from local storage with
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
        _logbookDownloadState.update { it.copy(savingError = false) }
        viewModelScope.launch {
            try {
                saveLogbookToChosenPathUseCase(logbookDownloadState.value.filename)

                _logbookDownloadState.update {
                    ru.skypaws.features.model.LogbookDownloadState(
                        logbookExpDate = it.logbookExpDate,
                        downloaded = true,
                        path = fileServiceStorage.transformPath(getPathUseCase())
                    )
                }
            } catch (e: Exception) {
                _logbookDownloadState.update { it.copy(savingError = true) }
            }
        }
    }
}

