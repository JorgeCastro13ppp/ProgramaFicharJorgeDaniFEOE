package com.example.programaficharfeoe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AzulHidrocaex,
    secondary = AzulOscuro,
    background = FondoClaro,
    surface = SuperficieClaro,
    onPrimary = TextoClaro,
    onSecondary = TextoClaro,
    onBackground = TextoOscuro,
    onSurface = TextoOscuro
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulClaro,
    secondary = AzulHidrocaex,
    background = FondoOscuro,
    surface = SuperficieOscuro,
    onPrimary = TextoClaro,
    onSecondary = TextoClaro,
    onBackground = TextoClaro,
    onSurface = TextoClaro
)

@Composable
fun ProgramaFicharFEOETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}