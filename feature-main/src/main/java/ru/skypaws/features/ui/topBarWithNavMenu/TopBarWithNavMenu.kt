package ru.skypaws.features.ui.topBarWithNavMenu

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.skypaws.mobileapp.data.utils.dbVersion
import ru.skypaws.mobileapp.data.utils.version
import ru.skypaws.mobileapp.domain.model.UserDomain
import ru.skypaws.presentation.R
import ru.skypaws.presentation.model.UpdateModel
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme
import ru.skypaws.presentation.ui.theme.update_available
import ru.skypaws.features.utils.crewPlanUI
import ru.skypaws.features.utils.enterUI
import ru.skypaws.features.utils.logbook
import ru.skypaws.features.utils.payServices
import ru.skypaws.features.utils.settings

private data class MenuItem(
    val route: String,
    val titleId: Int,
    val iconId: Int,
)

@Composable
fun NavMenu(
    title: String,
    isActionNeed: Boolean,
    navigateTo: (route: String) -> Unit,
    updateState: UpdateModel,
    user: UserDomain,
    installApk: () -> Unit,
    downloadApk: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val context = LocalContext.current

    val items = listOf(
        MenuItem(crewPlanUI, R.string.crew_plan, R.drawable.plan_airplane),
        MenuItem(logbook, R.string.logbook, R.drawable.logbook),
        MenuItem(payServices, R.string.paid_services, R.drawable.baseline_stars_24),
        //MenuItem(calendar, R.string.calendar, R.drawable.calendar),
        MenuItem(settings, R.string.settings, R.drawable.settings),
        MenuItem(enterUI, R.string.sign_out, 0)
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }

    // enable gesture to open/close Drawer from right to left only for closing
    val gesturesEnabled = remember { mutableStateOf(false) }
    LaunchedEffect(drawerState) {
        snapshotFlow { drawerState.isOpen }.collect { isOpen ->
            gesturesEnabled.value = isOpen
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled.value,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .windowInsetsPadding(WindowInsets.statusBars.add(WindowInsets.navigationBars)),
            )
            {
                Box(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Column {
                        NavigationHeader(user = user)

                        items.forEach { item ->
                            if (item.route == settings || item.route == enterUI) {
                                HorizontalDivider(thickness = 1.dp)
                            }

                            NavigationDrawerItem(
                                icon = {
                                    if (item.iconId != 0) {
                                        Icon(
                                            painter = painterResource(id = item.iconId),
                                            contentDescription = null,
                                        )
                                    }
                                },
                                label = {
                                    Text(text = stringResource(id = item.titleId))
                                },
                                selected = false,
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedTextColor = LocalCustomColors.current.blackWhite
                                ),
                                onClick = {
                                    if (item.route == enterUI) {
                                        openDialog.value = true
                                    } else {
                                        navigateTo(item.route)
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    }
                                }
                            )
                        }
                    }

                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 4.dp),
                        text = stringResource(id = R.string.version, "$version, $dbVersion"),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    if (updateState.newVersion && updateState.downloadProgress == 0f || updateState.downloaded) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    when {
                                        updateState.downloading -> {}
                                        updateState.downloaded -> installApk()
                                        else -> downloadApk()
                                    }
                                }
                                .align(Alignment.BottomCenter)
                                .background(update_available),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 16.dp, horizontal = 12.dp),
                                maxLines = 2,
                                text = when {
                                    updateState.downloaded -> {
                                        if (context is Activity) {
                                            stringResource(id = R.string.download_finished_update)
                                        } else {
                                            ""
                                        }
                                    }

                                    updateState.downloadError -> stringResource(id = R.string.download_error)
                                    updateState.downloading -> stringResource(id = R.string.downloading)
                                    else -> stringResource(id = R.string.update_avalable)
                                },
                                textAlign = TextAlign.Center,
                                color = Color.White,
                            )
                        }
                    }


                    if (updateState.downloading && updateState.downloadProgress != 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(update_available),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp, horizontal = 24.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = { updateState.downloadProgress / 100f },
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                )

                                Text(
                                    text = "${updateState.downloadProgress.toInt()}%",
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                )
                            }

                        }
                    }
                }
            }
        },
    ) {
        TopBar(
            title = title,
            navigateTo = navigateTo,
            drawerState = drawerState,
            scope = scope,
            isActionNeed = isActionNeed,
            isClickable = !updateState.newDB,
            content = { paddingValues ->
                content(paddingValues)
            }
        )
    }

    // Диалоговое окно для подтверждения выхода из ЛК
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = stringResource(id = R.string.sign_out_card_title)) },
            text = { Text(text = stringResource(id = R.string.areYouSure)) },
            confirmButton = {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(contentColor = LocalCustomColors.current.textButton),
                    onClick = {
                        openDialog.value = false
                        scope.launch {
                            navigateTo(enterUI)
                            drawerState.close()
                        }
                    }
                ) {
                    Text(stringResource(id = R.string.sign_out))
                }
            },
            dismissButton = {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(contentColor = LocalCustomColors.current.textButton),
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.back))
                }
            }
        )
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "ru",
    name = "Dark"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "ru",
    name = "Light"
)
@Composable
fun NavMenuPreview(
    title: String = "Logbook",
    isActionNeed: Boolean = true,
) {
    val updateState = UpdateModel()
    val context = LocalContext.current

    val items = listOf(
        MenuItem(crewPlanUI, R.string.crew_plan, R.drawable.plan_airplane),
        MenuItem(logbook, R.string.logbook, R.drawable.logbook),
        MenuItem(payServices, R.string.paid_services, R.drawable.baseline_stars_24),
        //MenuItem(calendar, R.string.calendar, R.drawable.calendar),
        MenuItem(settings, R.string.settings, R.drawable.settings),
        MenuItem(enterUI, R.string.sign_out, 0)
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val openDialog = remember { mutableStateOf(false) }

    // enable gesture to open/close Drawer from right to left only for closing
    val gesturesEnabled = remember { mutableStateOf(false) }
    LaunchedEffect(drawerState) {
        snapshotFlow { drawerState.isOpen }.collect { isOpen ->
            gesturesEnabled.value = isOpen
        }
    }

    SkyPawsTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = gesturesEnabled.value,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .windowInsetsPadding(WindowInsets.statusBars.add(WindowInsets.navigationBars)),
                )
                {
                    Box(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Column {
                            NavigationHeaderPreview()

                            items.forEach { item ->
                                if (item.route == settings || item.route == enterUI) {
                                    HorizontalDivider(thickness = 1.dp)
                                }

                                NavigationDrawerItem(
                                    icon = {
                                        if (item.iconId != 0) {
                                            Icon(
                                                painter = painterResource(id = item.iconId),
                                                contentDescription = null,
                                            )
                                        }
                                    },
                                    label = {
                                        Text(text = stringResource(id = item.titleId))
                                    },
                                    selected = false,
                                    colors = NavigationDrawerItemDefaults.colors(
                                        unselectedTextColor = LocalCustomColors.current.blackWhite
                                    ),
                                    onClick = {}
                                )
                            }
                        }

                        Text(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 4.dp),
                            text = stringResource(id = R.string.version, "$version, $dbVersion"),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        if (updateState.newVersion && updateState.downloadProgress == 0f || updateState.downloaded) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {}
                                    .align(Alignment.BottomCenter)
                                    .background(update_available),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 16.dp, horizontal = 12.dp),
                                    maxLines = 2,
                                    text = when {
                                        updateState.downloaded -> {
                                            if (context is Activity) {
                                                stringResource(id = R.string.download_finished_update)
                                            } else {
                                                ""
                                            }
                                        }

                                        updateState.downloadError -> stringResource(id = R.string.download_error)
                                        updateState.downloading -> stringResource(id = R.string.downloading)
                                        else -> stringResource(id = R.string.update_avalable)
                                    },
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                )
                            }
                        }


                        if (updateState.downloading && updateState.downloadProgress != 0f) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(update_available),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp, horizontal = 24.dp)
                                ) {
                                    LinearProgressIndicator(
                                        progress = { updateState.downloadProgress / 100f },
                                        modifier = Modifier
                                            .weight(1f)
                                            .align(Alignment.CenterVertically),
                                    )

                                    Text(
                                        text = "${updateState.downloadProgress.toInt()}%",
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                    )
                                }

                            }
                        }
                    }
                }
            },
        ) {
            TopBarPreview(
                title = title,
                isActionNeed = isActionNeed,
                isClickable = !updateState.newDB,
            )
        }

        // Диалоговое окно для подтверждения выхода из ЛК
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text(text = stringResource(id = R.string.sign_out_card_title)) },
                text = { Text(text = stringResource(id = R.string.areYouSure)) },
                confirmButton = {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(contentColor = LocalCustomColors.current.textButton),
                        onClick = {}
                    ) {
                        Text(stringResource(id = R.string.sign_out))
                    }
                },
                dismissButton = {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(contentColor = LocalCustomColors.current.textButton),
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text(stringResource(id = R.string.back))
                    }
                }
            )
        }
    }
}