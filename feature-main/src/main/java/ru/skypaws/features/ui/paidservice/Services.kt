package ru.skypaws.features.ui.paidservice

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.skypaws.presentation.R
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme
import ru.skypaws.features.utils.logbookPay

@Composable
fun Services(
    service: PaidServices,
    navigateTo: (route: String) -> Unit
) {
    var checkedStates by remember {
        mutableStateOf(List(service.priceDuration.size) { index -> index == 0 })
    }
    var showInfoCard by remember { mutableStateOf(false) }

    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp
    )

    Text(
        color = LocalCustomColors.current.blackWhite,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        fontWeight = FontWeight.Bold,
        text = service.name,
        maxLines = 1,
        textAlign = TextAlign.Start
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (service.isActive) {
            Text(
                color = LocalCustomColors.current.blackWhite,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                maxLines = 2,
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.paid_until, service.date ?: "")
            )

            Spacer(modifier = Modifier.width(2.dp))

            Box {
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
                            id = R.string.service_expire_UTC, service.date ?: ""
                        )
                    )
                }
            }

        } else {
            Column {
                service.priceDuration.forEachIndexed { index, priceDuration ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (service.priceDuration.size > 1) {
                            Checkbox(
                                checked = checkedStates[index],
                                onCheckedChange = {
                                    checkedStates = if (!checkedStates[index]) {
                                        checkedStates.toMutableList().apply {
                                            for (i in indices) {
                                                set(i, i == index)
                                            }
                                        }
                                    } else {
                                        checkedStates.toMutableList().apply {
                                            set(index, false)
                                        }
                                    }
                                }
                            )
                        }

                        Text(
                            color = LocalCustomColors.current.blackWhite,
                            modifier = Modifier
                                .weight(0.9f)
                                .padding(horizontal = 4.dp),
                            maxLines = 2,
                            textAlign = TextAlign.Start,
                            text = pluralStringResource(
                                id = R.plurals.durationMonth,
                                count = priceDuration.duration,
                                priceDuration.duration
                            )
                        )

                        Text(
                            color = LocalCustomColors.current.blackWhite,
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(horizontal = 4.dp),
                            maxLines = 2,
                            textAlign = TextAlign.Start,
                            text = stringResource(id = R.string.price, priceDuration.price)
                        )

                        if (index != 0) {
                            Text(
                                color = LocalCustomColors.current.blackWhite,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp),
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                text = stringResource(
                                    id = R.string.discount,
                                    (priceDuration.price -
                                            service.priceDuration[0].price *
                                            priceDuration.duration)
                                )
                            )
                        } else {
                            Box(
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }
    }

    if (!service.isActive) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier
                    .padding(8.dp),
                onClick = { navigateTo(service.route) }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_payment_24),
                        contentDescription = "payment icon"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(id = R.string.pay).uppercase()
                    )
                }
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
fun ServicesPrev(
    service: PaidServices = PaidServices(
        name = stringResource(id = R.string.logbook),
        isActive = false,
        date = "29.09.2024",
        priceDuration = listOf(
            PriceDuration(
                3000,
                1
            )
        ),
        route = logbookPay
    )
) {
    var checkedStates by remember {
        mutableStateOf(List(service.priceDuration.size) { index -> index == 0 })
    }
    var showInfoCard by remember { mutableStateOf(false) }
    SkyPawsTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp
            )

            Text(
                color = LocalCustomColors.current.blackWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontWeight = FontWeight.Bold,
                text = service.name,
                maxLines = 1,
                textAlign = TextAlign.Start
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (service.isActive) {
                    Text(
                        color = LocalCustomColors.current.blackWhite,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.paid_until, service.date ?: "")
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    Box {
                        IconButton(onClick = { showInfoCard = !showInfoCard }) {
                            Icon(
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
                                    id = R.string.service_expire_UTC, service.date ?: ""
                                )
                            )
                        }
                    }

                } else {
                    Column {
                        service.priceDuration.forEachIndexed { index, priceDuration ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (service.priceDuration.size > 1) {
                                    Checkbox(
                                        checked = checkedStates[index],
                                        onCheckedChange = {
                                            checkedStates = if (!checkedStates[index]) {
                                                checkedStates.toMutableList().apply {
                                                    for (i in indices) {
                                                        set(i, i == index)
                                                    }
                                                }
                                            } else {
                                                checkedStates.toMutableList().apply {
                                                    set(index, false)
                                                }
                                            }
                                        }
                                    )
                                }

                                Text(
                                    color = LocalCustomColors.current.blackWhite,
                                    modifier = Modifier
                                        .weight(0.9f)
                                        .padding(horizontal = 4.dp),
                                    maxLines = 2,
                                    textAlign = TextAlign.Start,
                                    text = pluralStringResource(
                                        id = R.plurals.durationMonth,
                                        count = priceDuration.duration,
                                        priceDuration.duration
                                    )
                                )

                                Text(
                                    color = LocalCustomColors.current.blackWhite,
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(horizontal = 4.dp),
                                    maxLines = 2,
                                    textAlign = TextAlign.Start,
                                    text = stringResource(id = R.string.price, priceDuration.price)
                                )

                                if (index != 0) {
                                    Text(
                                        color = LocalCustomColors.current.blackWhite,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 4.dp),
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                        text = stringResource(
                                            id = R.string.discount,
                                            (priceDuration.price -
                                                    service.priceDuration[0].price *
                                                    priceDuration.duration)
                                        )
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (!service.isActive) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        modifier = Modifier
                            .padding(8.dp),
                        onClick = { }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_payment_24),
                                contentDescription = "payment icon"
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = stringResource(id = R.string.pay).uppercase()
                            )
                        }
                    }
                }
            }
        }
    }
}