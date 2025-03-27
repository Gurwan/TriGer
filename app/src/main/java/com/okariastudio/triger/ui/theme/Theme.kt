package com.okariastudio.triger.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.okariastudio.triger.viewmodel.SettingsViewModel

val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Background,
    secondary = Secondary,
    onSecondary = Surface,
    background = Background,
    surface = Surface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    tertiary = TextSecondary,
    error = Error,
    outline = Success,
    onError = Background
)

val DarkColorScheme = darkColorScheme(
    primary = Background,
    onPrimary = Primary,
    secondary = Accent,
    onSecondary = Secondary,
    background = Primary,
    surface = Secondary,
    onBackground = Accent,
    tertiary = Accent,
    outline = Success,
    onSurface = Accent,
    error = Error,
    onError = Primary
)

@Composable
fun TriGerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    settingsViewModel: SettingsViewModel?,
    content: @Composable () -> Unit
) {
    if(settingsViewModel != null){
        val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState(darkTheme)

        val colorScheme = when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            isDarkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    } else {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            typography = Typography,
            content = content
        )
    }

}