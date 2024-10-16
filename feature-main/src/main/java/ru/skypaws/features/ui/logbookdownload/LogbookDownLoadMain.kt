package ru.skypaws.features.ui.logbookdownload

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.presentation.R
import ru.skypaws.features.model.LogbookDownloadState
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@Composable
fun LogbookDownLoadMain(
    downloadLogbookIntent: () -> Unit,
    state: LogbookDownloadState,
    logbookList: List<Int>,
    snackbarHostState: SnackbarHostState,
) {
    var showInfoCard by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            // service info (paid state and expiration date)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Expiration Date
                Row {
                    Text(
                        color = LocalCustomColors.current.blackWhite,
                        text = stringResource(
                            id = R.string.activated_until,
                            state.logbookExpDate
                        ),
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp,
                        textAlign = TextAlign.Center
                    )

                    Box(modifier = Modifier.align(Alignment.Bottom)) {
                        IconButton(onClick = { showInfoCard = !showInfoCard }) {
                            Icon(
                                tint = LocalCustomColors.current.blackWhite,
                                painter = painterResource(id = R.drawable.baseline_info_outline_24),
                                contentDescription = "information"
                            )
                        }

                        DropdownMenu(
                            expanded = showInfoCard,
                            onDismissRequest = { showInfoCard = false },
                            modifier = Modifier.fillMaxWidth(0.6f)
                        ) {
                            Text(
                                color = LocalCustomColors.current.blackWhite,
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = stringResource(
                                    id = R.string.service_expire_UTC, state.logbookExpDate
                                )
                            )
                        }
                    }
                }
            }

            // Main info and functions for logbook download service
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary
                )

                // UI with main info and functions for logbook download service
                LogbookDownloadPart(
                    list = logbookList,
                    downloadLogbookIntent = downloadLogbookIntent
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary
                )
            }


            // Update button to make new logbooks
            Button(
                onClick = { }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_article_24),
                        contentDescription = "payment icon"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(id = R.string.update).uppercase(),
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )
                }
            }
        }

        // Progress bar during logbook downloading (saving to cacheDir)
        if (state.downloading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LocalCustomColors.current.shadow),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // SnackBar with error
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomCenter)
        )
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
fun LogbookDownLoadNavMenuPreview(
    state: LogbookDownloadState = LogbookDownloadState(
        logbookExpDate = "10.08.2024"
    ),
    logbookList: List<Int> = listOf(
        R.string.excel,
        R.string.easa,
//        R.string.faa
    ),
    snackbarHostState: SnackbarHostState = SnackbarHostState()
) {
    SkyPawsTheme {
        var showInfoCard by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                // service info (paid state and expiration date)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Expiration Date
                    Row {
                        Text(
                            color = LocalCustomColors.current.blackWhite,
                            text = stringResource(
                                id = R.string.activated_until,
                                state.logbookExpDate
                            ),
                            fontSize = dimensionResource(id = R.dimen.normal_text).value.sp,
                            textAlign = TextAlign.Center
                        )

                        Box(modifier = Modifier.align(Alignment.Bottom)) {
                            IconButton(onClick = { showInfoCard = !showInfoCard }) {
                                Icon(
                                    tint = LocalCustomColors.current.blackWhite,
                                    painter = painterResource(id = R.drawable.baseline_info_outline_24),
                                    contentDescription = "information"
                                )
                            }

                            DropdownMenu(
                                expanded = showInfoCard,
                                onDismissRequest = { showInfoCard = false },
                                modifier = Modifier.fillMaxWidth(0.6f)
                            ) {
                                Text(
                                    color = LocalCustomColors.current.blackWhite,
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    text = stringResource(
                                        id = R.string.service_expire_UTC, state.logbookExpDate
                                    )
                                )
                            }
                        }
                    }
                }

                // Main info and functions for logbook download service
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.primary
                    )

                    // UI with main info and functions for logbook download service
                    LogbookDownloadPartPreview()

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.primary
                    )
                }


                // Update button to make new logbooks
                Button(
                    onClick = { }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_article_24),
                            contentDescription = "payment icon"
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(id = R.string.update).uppercase(),
                            fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                        )
                    }
                }
            }

            // Progress bar during logbook downloading (saving to cacheDir)
            if (state.downloading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LocalCustomColors.current.shadow),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // SnackBar with error
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}



