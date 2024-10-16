package ru.skypaws.features.ui.topBarWithNavMenu

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ru.skypaws.presentation.R
import ru.skypaws.features.viewmodel.TopBarViewModel
import ru.skypaws.features.utils.logbookDownload
import ru.skypaws.features.utils.logbookPay

@Composable
fun CustomDropDownMenu(
    title: String,
    isExpanded: Boolean,
    topBarViewModel: TopBarViewModel = hiltViewModel(),
    navigateTo: (route: String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val state by topBarViewModel.topBarState.collectAsState()

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { onDismissRequest() }
    ) {
        val logbookItems = listOf(
            R.string.logbook_download,
        )

        // menu items depending on ui title
        val menuItems = when (title) {
            stringResource(id = R.string.logbook) -> logbookItems
            else -> emptyList()
        }

        val isPaid = when (title) {
            stringResource(id = R.string.logbook) -> {
                topBarViewModel.logbookPaidStatus()
                state.isLogbookPaid
            }

            else -> false
        }

        menuItems.forEach { item ->
            DropdownMenuItem(
                text = { Text(text = stringResource(id = item)) },
                onClick = {
                    getAction(
                        item = item,
                        isPaid = isPaid,
                        navigateTo = navigateTo
                    )
                    onDismissRequest()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomDropDownMenuPreview(
    title: String = "Logbook",
    isExpanded: Boolean = true,
    isPaid: Boolean = false,
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { }
    ) {
        val logbookItems = listOf(
            R.string.logbook_download,
        )

        // menu items depending on ui title
        val menuItems = when (title) {
            stringResource(id = R.string.logbook) -> logbookItems
            else -> emptyList()
        }

        menuItems.forEach { item ->
            DropdownMenuItem(
                text = { Text(text = stringResource(id = item)) },
                onClick = {}
            )
        }
    }
}

private fun getAction(
    item: Int,
    isPaid: Boolean = false,
    navigateTo: (route: String) -> Unit
) {
    when (item) {
        R.string.logbook_download -> {
            if (isPaid) {
                navigateTo(logbookDownload)
            } else {
                navigateTo(logbookPay)
            }
        }
    }
}
