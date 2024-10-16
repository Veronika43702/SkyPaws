package ru.skypaws.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    // text, buttonEnabled
    primary = light_primary,
    // text on button
    onPrimary = light_onPrimary,
    primaryContainer = light_primaryContainer,
    onPrimaryContainer = light_onPrimaryContainer,
    // progress Bar
    secondary = light_secondary,
    // circle round icon
    onSecondary = light_onSecondary,
    // recyclerView item background
    secondaryContainer = light_secondaryContainer,
    // text on recyclerView item
    onSecondaryContainer = light_onSecondaryContainer,
    tertiary = light_tertiary,
    onTertiary = light_onTertiary,
    tertiaryContainer = light_tertiaryContainer,
    onTertiaryContainer = light_onTertiaryContainer,
    error = light_error,
    errorContainer = light_errorContainer,
    onError = light_onError,
    onErrorContainer = light_onErrorContainer,
    // background
    background = light_background,
    onBackground = light_onBackground,
    // Dialog, topAppBar background
    surface = light_surface,
    // EditText, AutoEditText, ButtonDisabled
    onSurface = light_onSurface,
    surfaceVariant = light_surfaceVariant,
    // hintText, endIcon, labledEditText, dialogVariants
    onSurfaceVariant = light_onSurfaceVariant,
    // outlineOfEditText
    outline = light_outline,
    inverseOnSurface = light_inverseOnSurface,
    // snackbar background
    inverseSurface = light_inverseSurface,
    // buttons on snackbar
    inversePrimary = light_inversePrimary,
    surfaceTint = light_surfaceTint,
    // dividers
    outlineVariant = light_outlineVariant,
    // shadow on background (ui under navigation menu)
    scrim = light_scrim,
    // NavMenuBackground
    surfaceContainerLow = light_surfaceContainerLow,
    // menuBackground
    surfaceContainer = light_surfaceContainer,
    surfaceContainerHigh = light_surfaceContainerHigh,
)

private val DarkColorScheme = darkColorScheme(
    // text, buttonEnabled
    primary = dark_primary,
    // text on button
    onPrimary = dark_onPrimary,
    primaryContainer = dark_primaryContainer,
    onPrimaryContainer = dark_onPrimaryContainer,
    // progress Bar
    secondary = dark_secondary,
    // circle round icon
    onSecondary = dark_onSecondary,
    // recyclerView item  background
    secondaryContainer = dark_secondaryContainer,
    // text on recyclerView item
    onSecondaryContainer = dark_onSecondaryContainer,
    tertiary = dark_tertiary,
    onTertiary = dark_onTertiary,
    tertiaryContainer = dark_tertiaryContainer,
    onTertiaryContainer = dark_onTertiaryContainer,
    error = dark_error,
    errorContainer = dark_errorContainer,
    onError = dark_onError,
    onErrorContainer = dark_onErrorContainer,
    // background
    background = dark_background,
    onBackground = dark_onBackground,
    // Dialog, topAppBar background
    surface = dark_surface,
    // EditText, AutoEditText, ButtonDisabled
    onSurface = dark_onSurface,
    // Card background
    surfaceVariant = dark_surfaceVariant,
    // hintText, endIcon, labledEditText, dialogVariants
    onSurfaceVariant = dark_onSurfaceVariant,
    // outlineOfEditText
    outline = dark_outline,
    inverseOnSurface = dark_inverseOnSurface,
    // snackbar background
    inverseSurface = dark_inverseSurface,
    // buttons on snackbar
    inversePrimary = dark_inversePrimary,
    surfaceTint = dark_surfaceTint,
    // dividers
    outlineVariant = dark_outlineVariant,
    scrim = dark_scrim,
    // NavMenuBackground
    surfaceContainerLow = dark_surfaceContainerLow,
    // menuBackground
    surfaceContainer = dark_surfaceContainer,
    surfaceContainerHigh = dark_surfaceContainerHigh,
)

data class CustomColors(
    val blackWhite: Color = Color.Unspecified,
    val shadow: Color = Color.Unspecified,
    val textButton: Color = Color.Unspecified,
    val settingBackground: Color = Color.Unspecified,
    val settingChosen: Color = Color.Unspecified,
    val settingIcon: Color = Color.Unspecified,
    val flightBackgroundExpand: Color = Color.Unspecified,
    val logbookTotal: Color = Color.Unspecified,

    val logbookYear: Color = Color.Unspecified,
    val logbookMonth: Color = Color.Unspecified,
    val logbookFlight: Color = Color.Unspecified,

)

private val LightCustomColors = CustomColors(
    blackWhite = Color.Black,
    shadow = light_shadow,
    textButton = light_textButton,
    settingBackground = light_setting_background,
    settingChosen = light_setting_chosen,
    settingIcon = light_setting_icon,
    flightBackgroundExpand = light_flightBackgroundExpand,
    logbookTotal = light_logbook_total,
    logbookYear = light_logbook_year,
    logbookMonth = light_logbook_month,
    logbookFlight = light_logbook_flight,
)

private val DarkCustomColors = CustomColors(
    blackWhite = Color.White,
    shadow = dark_shadow,
    textButton = dark_textButton,
    settingBackground = dark_setting_background,
    settingChosen = dark_setting_chosen,
    settingIcon = dark_setting_icon,
    flightBackgroundExpand = dark_flightBackgroundExpand,
    logbookTotal = dark_logbook_total,
    logbookYear = dark_logbook_year,
    logbookMonth = dark_logbook_month,
    logbookFlight = dark_logbook_flight,
)

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

@Composable
fun SkyPawsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (!darkTheme) {
        LightColorScheme
    } else {
        DarkColorScheme
    }

    val customColors = if (!darkTheme) {
        LightCustomColors
    } else {
        DarkCustomColors
    }

    CompositionLocalProvider(
        LocalCustomColors provides customColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}