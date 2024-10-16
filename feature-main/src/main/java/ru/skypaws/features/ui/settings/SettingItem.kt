package ru.skypaws.features.ui.settings

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
import ru.skypaws.presentation.R
import ru.skypaws.presentation.ui.theme.LocalCustomColors
import ru.skypaws.presentation.ui.theme.SkyPawsTheme

@Composable
fun SettingItem(
    item: SettingItem,
    selectedOption: Int?,
    selectedPath: String?,
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

            if (item.title == R.string.path_to_download && selectedPath != null) {
                Text(
                    text = selectedPath,
                    maxLines = 2,
                    color = LocalCustomColors.current.settingChosen,
                    fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                )
            } else if (selectedOption != null) {
                Text(
                    text = when (item.title) {
                        // Theme:
                        //    1 - light
                        //    2 - dark
                        //    0 - system
                        R.string.theme -> stringResource(
                            id = when (selectedOption) {
                                1 -> R.string.light
                                2 -> R.string.dark
                                else -> R.string.system
                            }
                        )

                        // Airport code:
                        //    3 - IATA (LED)
                        //    5 - ICAO (ULLI)
                        //    1 - Inside code (ПЛК)
                        R.string.airport_type_to_view -> stringResource(
                            id = when (selectedOption) {
                                3 -> R.string.iata
                                5 -> R.string.icao
                                else -> R.string.inside_code
                            }
                        )

                        else -> ""
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
//    item: SettingItem = SettingItem(
//        icon = R.drawable.baseline_folder_open_24,
//        title = R.string.path_to_download,
//    ),
//    item: SettingItem = SettingItem(
//        icon = R.drawable.theme_icon,
//        title = R.string.theme,
//        listOfOptions = listOf(
//            R.string.system,
//            R.string.light,
//            R.string.dark
//        ),
//        cardTitle = R.string.choose_theme
//    ),
    item: SettingItem = SettingItem(
        icon = R.drawable.airport,
        title = R.string.airport_type_to_view,
        listOfOptions = listOf(
            R.string.iata,
            R.string.icao,
            R.string.inside_code
        ),
    ),
    selectedPath: String? = "path",
    selectedOption: Int? = 2,
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

                if (item.title == R.string.path_to_download && selectedPath != null) {
                    Text(
                        text = selectedPath,
                        maxLines = 2,
                        color = LocalCustomColors.current.settingChosen,
                        fontSize = dimensionResource(id = R.dimen.normal_text).value.sp
                    )
                } else if (selectedOption != null) {
                    Text(
                        text = when (item.title) {
                            // Theme:
                            //    1 - light
                            //    2 - dark
                            //    0 - system
                            R.string.theme -> stringResource(
                                id = when (selectedOption) {
                                    1 -> R.string.light
                                    2 -> R.string.dark
                                    else -> R.string.system
                                }
                            )

                            // Airport code:
                            //    3 - IATA (LED)
                            //    5 - ICAO (ULLI)
                            //    1 - Inside code (ПЛК)
                            R.string.airport_type_to_view -> stringResource(
                                id = when (selectedOption) {
                                    3 -> R.string.iata
                                    5 -> R.string.icao
                                    else -> R.string.inside_code
                                }
                            )

                            else -> ""
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