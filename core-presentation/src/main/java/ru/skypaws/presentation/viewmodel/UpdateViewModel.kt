package ru.skypaws.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.data.utils.FileStorageService
import ru.skypaws.mobileapp.data.utils.dbVersion
import ru.skypaws.mobileapp.data.utils.version
import ru.skypaws.mobileapp.domain.usecase.update.CheckUpdatesUseCase
import ru.skypaws.mobileapp.domain.usecase.update.DownloadApkUseCase
import ru.skypaws.presentation.model.UpdateModel
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val checkUpdatesUseCase: CheckUpdatesUseCase,
    private val downloadApkUseCase: DownloadApkUseCase,
    private val fileServiceStorage: FileStorageService,
) : ViewModel() {
    private val _updateState = MutableStateFlow(UpdateModel())
    val updateState: StateFlow<UpdateModel> = _updateState.asStateFlow()

    /**
     * Sets [updateState.loading][_updateState] in [UpdateViewModel] to **true**
     *
     * Calls function [UpdateRepository.checkUpdate()][ru.skypaws.mobileapp.repository.UpdateRepository.fetchLatestVersionInfo]
     * to get data from server of new app and database versions.
     *
     * In case
     * - there's new version AND new database: [updateState.newDB][_updateState] = **true**
     * - there's new version AND NO change in database: [updateState.newVersion][_updateState] = **true**
     *
     * Independently whether there's an exception or api request is successfully finished:
     *  [updateState.loading][_updateState] = **false** and
     *  [updateState.checkUpdatesFinished][_updateState] = **true**
     */
    fun checkUpdates() {
        _updateState.value =
            _updateState.value.copy(loading = true)
        viewModelScope.launch {
            try {
                val update = checkUpdatesUseCase()

                if (update.version != version) {
                    if (update.revision > dbVersion) {
                        _updateState.value =
                            _updateState.value.copy(newDB = true)
                    } else {
                        _updateState.value =
                            _updateState.value.copy(newVersion = true)
                    }
                }
            } catch (_: Exception) {
            } finally {
                _updateState.value =
                    _updateState.value.copy(
                        loading = false,
                        checkUpdatesFinished = true
                    )
            }
        }
    }

    /**
     * Sets [updateState.value.checkUpdatedFinished][_updateState]
     * in [UpdateViewModel] to **false**
     */
    fun setInitialUpdateState() {
        _updateState.value =
            _updateState.value.copy(checkUpdatesFinished = false)
    }


    /**
     * Sets [updateState.downloading][_updateState] to **true**.
     *
     * Calls function [UpdateRepositoryImpl.downloadApk()][ru.skypaws.mobileapp.repository.UpdateRepositoryImpl.downloadApk]
     * to get latest app-release.apk from server.
     *
     * In case
     * - download progress within (0;100) excluded: [downloadProgress][_updateState] = [progress][Float]
     * - download progress = 100: [downloaded][_updateState] = **true**,
     * [downloading][_updateState] = **false**
     * - exception: [downloadError][_updateState] = **true**,
     * [downloading][_updateState] = **false**
     */
    fun downloadApk() {
        _updateState.value =
            _updateState.value.copy(downloading = true)
        viewModelScope.launch {
            try {
                downloadApkUseCase().collect { progress ->
                    if (progress.toInt() == 100) {
                        _updateState.value =
                            _updateState.value.copy(
                                downloading = false,
                                downloaded = true
                            )
                    } else {
                        _updateState.value =
                            _updateState.value.copy(
                                downloading = true,
                                downloadProgress = progress
                            )
                    }
                }
            } catch (_: Exception) {
                _updateState.value =
                    _updateState.value.copy(
                        downloading = false,
                        downloadError = true
                    )
            }
        }
    }

    /**
     * Sets [updateState.downloaded][_updateState] in [UpdateViewModel] to **false**
     *
     * Calls function [FileStorageService.installApk][ru.skypaws.mobileapp.utils.FileStorageService.installApk]
     * to install file **app-release.apk** from **context.cacheDir**
     *
     * @param activity The activity context.
     */
    fun installApk(activity: Activity) {
        _updateState.value =
            _updateState.value.copy(downloaded = false)
        fileServiceStorage.installApk(activity)
    }
}