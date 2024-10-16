package ru.skypaws.features.ui.topBarWithNavMenu

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.skypaws.presentation.R
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    navigateTo: (route: String) -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope,
    isActionNeed: Boolean,
    isClickable: Boolean,
    content: @Composable (PaddingValues) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var isMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Box(
                modifier = Modifier
                    .height(90.dp)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                CenterAlignedTopAppBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                    ),
                    title = {
                        Text(
                            color = Color.White,
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        if (isClickable) {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) {
                                            open()

                                        } else {
                                            close()
                                        }
                                    }
                                }
                            }) {
                                Icon(
                                    tint = Color.White,
                                    painter = painterResource(R.drawable.menu),
                                    contentDescription = "navigation menu icon"
                                )
                            }
                        }
                    },
                    actions = {
                        if (isActionNeed) {
                            IconButton(
                                onClick = {
                                    isMenuExpanded = !isMenuExpanded
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.menu_overflow),
                                    contentDescription = "menu",
                                    tint = Color.White
                                )
                            }

                            CustomDropDownMenu(
                                title = title,
                                navigateTo = navigateTo,
                                isExpanded = isMenuExpanded,
                                onDismissRequest = { isMenuExpanded = false }
                            )

                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
        content = { innerPadding ->
            content(innerPadding)
        }
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "dark"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "light"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarPreview(
    title: String = "Logbook",
    isActionNeed: Boolean = true,
    isClickable: Boolean = true,
    viewModel: ViewModel? = null,
) {
    SkyPawsTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        var isMenuExpanded by remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                Box(
                    modifier = Modifier
                        .height(90.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    CenterAlignedTopAppBar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = Color.White,
                        ),
                        title = {
                            Text(
                                color = Color.White,
                                text = title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            if (isClickable) {
                                IconButton(onClick = {}) {
                                    Icon(
                                        tint = Color.White,
                                        painter = painterResource(R.drawable.menu),
                                        contentDescription = "navigation menu icon"
                                    )
                                }
                            }
                        },
                        actions = {
                            if (isActionNeed) {
                                IconButton(
                                    onClick = {
                                        isMenuExpanded = !isMenuExpanded
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.menu_overflow),
                                        contentDescription = "menu",
                                        tint = Color.White
                                    )
                                }

                                CustomDropDownMenuPreview(
                                    title = title,
                                    isExpanded = isMenuExpanded,
                                )

                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                }
            },
            content = { innerPadding ->
                Text(
                    text = "content",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        )
    }
}