package ru.skypaws.features.ui.logbookdownload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ru.skypaws.presentation.R
import ru.skypaws.features.viewmodel.LogbookDownloadIntent
import ru.skypaws.features.viewmodel.LogbookDownloadViewModel

@Composable
fun LogbookDownLoad(
    logbookDownloadViewModel: LogbookDownloadViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        logbookDownloadViewModel.handleIntent(LogbookDownloadIntent.GetLogbookExpData)
    }
    val logbookDownloadState by logbookDownloadViewModel.logbookDownloadState.collectAsState()

    // Launcher for file manager
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri: Uri? ->
        if (uri != null) {
            // start to copy file from temporary path to chosen by user
            logbookDownloadViewModel.handleIntent(LogbookDownloadIntent.SaveLogbook(uri))
        }
    }

    // snackbar during error of logbook donwloading
    val snackbarHostState = remember { SnackbarHostState() }
    val textError = stringResource(id = R.string.serverNotWork)
    val textDownloaded =
        stringResource(id = R.string.fileDownloaded, logbookDownloadState.path ?: "")
    val snackBarButtonName = stringResource(id = R.string.retry).uppercase()

    LaunchedEffect(logbookDownloadState) {
        when {
            logbookDownloadState.downloaded ->
                snackbarHostState.showSnackbar(
                    message = textDownloaded,
                    duration = SnackbarDuration.Long,
                    withDismissAction = true,
                )

            logbookDownloadState.downloadError -> {
                val result = snackbarHostState.showSnackbar(
                    message = textError,
                    duration = SnackbarDuration.Long,
                    withDismissAction = true,
                    actionLabel = snackBarButtonName,
                )

                if (result == SnackbarResult.ActionPerformed) {
                    // download Logbook to temporary path
                    logbookDownloadViewModel.handleIntent(LogbookDownloadIntent.DownloadLogbook)
                }
            }


            // if logbook is downloaded in cacheDir ->
            // open file manager or download to chosen path (set in settings)
            logbookDownloadState.downloadedTemp && logbookDownloadState.isPathSet -> {
                // if file is downloaded to temporary path and path is set in settings ->
                // copy temporary file to chosen path
                logbookDownloadViewModel.handleIntent(LogbookDownloadIntent.SaveLogbookToChosenPath)
            }

            logbookDownloadState.downloadedTemp -> {
                // if file is downloaded to temporary path -> filePicker to choose the path
                filePickerLauncher.launch(logbookDownloadState.filename)
            }
        }
    }

    val logbookList = listOf(
        R.string.excel,
        R.string.easa,
//        R.string.faa
    )

    LogbookDownLoadMain(
        downloadLogbookIntent = { logbookDownloadViewModel.handleIntent(LogbookDownloadIntent.DownloadLogbook) },
        state = logbookDownloadState,
        logbookList = logbookList,
        snackbarHostState = snackbarHostState,
    )
}



