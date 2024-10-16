package ru.skypaws.features.mainActivity.ui.topBarWithNavMenu

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import ru.skypaws.features.R
import ru.skypaws.features.mainActivity.viewmodel.LogbookViewModel
import ru.skypaws.features.util.logbookDownload
import ru.skypaws.features.util.logbookPay

@Composable
fun CustomDropDownMenu(
    title: String,
    isExpanded: Boolean,
    viewModel: ViewModel?,
    navigateTo: (route: String) -> Unit,
    onDismissRequest: () -> Unit,
) {
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

        menuItems.forEach { item ->
            DropdownMenuItem(
                text = { Text(text = stringResource(id = item)) },
                onClick = {
                    getAction(
                        item = item,
                        viewModel = viewModel,
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
    viewModel: ViewModel? = null,
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
    viewModel: ViewModel?,
    navigateTo: (route: String) -> Unit
) {
    when (viewModel) {
        // for Logbook UI (LogbookViewModel)
        is LogbookViewModel -> {
            when (item) {
                R.string.logbook_download -> {
                    if (viewModel.logbookPaidStatus()) {
                        navigateTo(logbookDownload)
                    } else {
                        navigateTo(logbookPay)
                    }
                }
            }
        }
    }
}
