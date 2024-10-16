package ru.skypaws.features.ui.logbookdownload

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.presentation.R
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

// UI with main info and functions for logbook download service
@Composable
fun LogbookDownloadPart(
    list: List<Int>,
    downloadLogbookIntent: () -> Unit,
) {
    var selectMode by remember { mutableStateOf(false) }
    var checkedStates by remember { mutableStateOf(List(list.size) { false }) }
    var showInfoCard by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.Start,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                color = LocalCustomColors.current.blackWhite,
                text = stringResource(id = R.string.logbook_download)
            )

            Box {
                IconButton(onClick = { showInfoCard = !showInfoCard }) {
                    Icon(
                        tint = LocalCustomColors.current.blackWhite,
                        painter = painterResource(id = R.drawable.baseline_info_24),
                        contentDescription = "information"
                    )
                }

                DropdownMenu(
                    expanded = showInfoCard,
                    onDismissRequest = { showInfoCard = false },
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text(
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = stringResource(id = R.string.logbook_warning)
                    )
                }
            }
        }

        // Select button to activate/inactivate selection mode
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = {
                    if (selectMode) {
                        checkedStates = checkedStates.map { false }
                    }

                    selectMode = !selectMode
                }
            ) {
                Text(
                    text = if (selectMode) {
                        stringResource(id = R.string.cancel)
                    } else {
                        stringResource(id = R.string.select)
                    },
                    color = LocalCustomColors.current.textButton,
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                )
            }
        }

        // list of logbook items (Excel, PDFs)
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            list.forEachIndexed { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (selectMode) {
                        Checkbox(
                            checked = checkedStates[index],
                            onCheckedChange = {
                                checkedStates =
                                    checkedStates.toMutableList().apply { set(index, it) }
                            }
                        )

                        Text(
                            color = LocalCustomColors.current.blackWhite,
                            modifier = Modifier
                                .weight(1f),
                            text = stringResource(id = item),
                            fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                        )
                    } else {
                        Spacer(modifier = Modifier.width(12.dp))

                        Row(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(0.7f)
                                .clickable {
                                    // download Logbook to temporary path
                                    downloadLogbookIntent()
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                color = LocalCustomColors.current.blackWhite,
                                text = stringResource(id = item),
                                fontSize = dimensionResource(id = R.dimen.normal_text).value.sp,
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Icon(
                                tint = LocalCustomColors.current.blackWhite,
                                painter = painterResource(id = R.drawable.baseline_download_24),
                                contentDescription = "download icon",
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        // Button to download selected logbooks
        if (selectMode) {
            TextButton(
                onClick = {
                    //TODO
                }
            ) {
                Text(
                    text = stringResource(id = R.string.download),
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp,
                    color = LocalCustomColors.current.textButton
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_download_24),
                    contentDescription = "payment icon",
                    tint = LocalCustomColors.current.textButton
                )
            }
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
fun LogbookDownloadPartPreview(
    list: List<Int> = listOf(
        R.string.excel,
        R.string.easa,
        R.string.faa
    ),
) {
    var selectMode by remember { mutableStateOf(false) }
    var checkedStates by remember { mutableStateOf(List(list.size) { false }) }
    var showInfoCard by remember { mutableStateOf(false) }

    SkyPawsTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start,
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    color = LocalCustomColors.current.blackWhite,
                    text = stringResource(id = R.string.logbook_download)
                )

                Box {
                    IconButton(onClick = { showInfoCard = !showInfoCard }) {
                        Icon(
                            tint = LocalCustomColors.current.blackWhite,
                            painter = painterResource(id = R.drawable.baseline_info_24),
                            contentDescription = "information"
                        )
                    }

                    DropdownMenu(
                        expanded = showInfoCard,
                        onDismissRequest = { showInfoCard = false },
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        Text(
                            color = LocalCustomColors.current.blackWhite,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = stringResource(id = R.string.logbook_warning)
                        )
                    }
                }
            }

            // Select button to activate/inactivate selection mode
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = {
                        if (selectMode) {
                            checkedStates = checkedStates.map { false }
                        }

                        selectMode = !selectMode
                    }
                ) {
                    Text(
                        text = if (selectMode) {
                            stringResource(id = R.string.cancel)
                        } else {
                            stringResource(id = R.string.select)
                        },
                        color = LocalCustomColors.current.textButton,
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )
                }
            }

            // list of logbook items (Excel, PDFs)
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                list.forEachIndexed { index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (selectMode) {
                            Checkbox(
                                checked = checkedStates[index],
                                onCheckedChange = {
                                    checkedStates =
                                        checkedStates.toMutableList().apply { set(index, it) }
                                }
                            )

                            Text(
                                color = LocalCustomColors.current.blackWhite,
                                modifier = Modifier
                                    .weight(1f),
                                text = stringResource(id = item),
                                fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                            )
                        } else {
                            Spacer(modifier = Modifier.width(12.dp))

                            Row(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .fillMaxWidth(0.7f)
                                    .clickable {},
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    color = LocalCustomColors.current.blackWhite,
                                    text = stringResource(id = item),
                                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp,
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Icon(
                                    tint = LocalCustomColors.current.blackWhite,
                                    painter = painterResource(id = R.drawable.baseline_download_24),
                                    contentDescription = "download icon",
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Button to download selected logbooks
            if (selectMode) {
                TextButton(
                    onClick = {
                        //TODO
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.download),
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp,
                        color = LocalCustomColors.current.textButton
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_download_24),
                        contentDescription = "payment icon",
                        tint = LocalCustomColors.current.textButton
                    )
                }
            }
        }
    }
}