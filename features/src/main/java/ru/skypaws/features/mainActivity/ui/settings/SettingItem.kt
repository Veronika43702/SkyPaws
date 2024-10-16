package ru.skypaws.features.mainActivity.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.features.R
import ru.skypaws.features.ui.theme.LocalCustomColors
import ru.skypaws.features.ui.theme.SkyPawsTheme

@Composable
fun SettingItem(
    item: SettingItem,
    selectedOption: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(20.dp))
            .background(LocalCustomColors.current.settingBackground)
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Icon(
            modifier = Modifier
                .padding(12.dp)
                .size(40.dp),
            painter = painterResource(id = item.icon),
            contentDescription = "theme icon",
            tint = LocalCustomColors.current.settingIcon
        )

        Spacer(modifier = Modifier.width(4.dp))

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(id = item.title),
                fontWeight = FontWeight.Bold,
                color = LocalCustomColors.current.blackWhite,
                fontSize = dimensionResource(id = R.dimen.settings_titles).value.sp
            )

            if (selectedOption != null) {
                Text(
                    // for path (viewmodel state) -> always current selected option
                    text = if (item.title == R.string.path_to_download) {
                        selectedOption
                        // for other settings -> selectedOption
                    } else {
                        stringResource(id = selectedOption.toInt())
                    },
                    maxLines = 2,
                    color = LocalCustomColors.current.settingChosen,
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
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
fun SettingItemPreview(
    item: SettingItem = SettingItem(
        icon = R.drawable.theme_icon,
        title = R.string.theme,
        listOfOptions = listOf(
            R.string.system,
            R.string.light,
            R.string.dark
        ),
        cardTitle = R.string.choose_theme
    ),
    selectedOption: String? = R.string.dark.toString(),
) {
    SkyPawsTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clip(RoundedCornerShape(20.dp))
                .background(LocalCustomColors.current.settingBackground)
                .padding(vertical = 8.dp)
                .clickable { }
        ) {
            Icon(
                modifier = Modifier
                    .padding(12.dp)
                    .size(40.dp),
                painter = painterResource(id = item.icon),
                contentDescription = "theme icon",
                tint = LocalCustomColors.current.settingIcon
            )

            Spacer(modifier = Modifier.width(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = stringResource(id = item.title),
                    fontWeight = FontWeight.Bold,
                    color = LocalCustomColors.current.blackWhite,
                    fontSize = dimensionResource(id = R.dimen.settings_titles).value.sp
                )

                if (selectedOption != null) {
                    Text(
                        // for path (viewmodel state) -> always current selected option
                        text = if (item.title == R.string.path_to_download) {
                            selectedOption
                            // for other settings -> selectedOption
                        } else {
                            stringResource(id = selectedOption.toInt())
                        },
                        maxLines = 2,
                        color = LocalCustomColors.current.settingChosen,
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )
                }
            }
        }
    }
}