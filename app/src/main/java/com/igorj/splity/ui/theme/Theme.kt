package com.igorj.splity.ui.theme

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val darkColorScheme = darkColorScheme(
    primary = Green,
    secondary = White,
    secondaryContainer = DarkGrey,
    tertiary = LightGrey,
    background = Black,
)

private val lightColorScheme = lightColorScheme(
    primary = Green,
    secondary = White,
    secondaryContainer = DarkGrey,
    tertiary = LightGrey,
    background = LightGrey,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val localColorScheme: ColorScheme
    @Composable get() = LocalColorSchemeProvider.current.colorScheme(LocalContext.current)

@Composable
fun SplityTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> darkColorScheme
      else -> lightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.background.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
    }

    MaterialTheme(
      colorScheme = colorScheme,
      typography = typography,
      content = content
    )
}

internal interface ColorSchemeProvider {
    fun colorScheme(context: Context): ColorScheme
}

internal val LocalColorSchemeProvider = staticCompositionLocalOf<ColorSchemeProvider> {
    object : ColorSchemeProvider {
        override fun colorScheme(context: Context): ColorScheme {
            return currentColorScheme(context)
        }
    }
}

fun isDarkMode(context: Context): Boolean {
    return (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}

fun currentColorScheme(context: Context): ColorScheme {
    return if (isDarkMode(context)) {
        darkColorScheme
    } else {
        lightColorScheme
    }
}
