package com.nutriweek.inclusiverecip.core.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.nutriweek.inclusiverecip.core.theme.Surface as SurfaceColor
import com.nutriweek.inclusiverecip.core.theme.OnError as OnErrorColor

private val HighContrastScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    background = Background,
    onBackground = OnBackground,
    surface = SurfaceColor,
    onSurface = OnSurface,
    error = Error,
    onError = OnErrorColor
)

@Composable
fun AccessRecetasTheme(
    content: @Composable () -> Unit
) {
    // Respeta escalado de fuentes del sistema (TalkBack/Settings)
    val typography = AppTypography

    MaterialTheme(
        colorScheme = HighContrastScheme,
        typography = typography,
        content = content
    )
}