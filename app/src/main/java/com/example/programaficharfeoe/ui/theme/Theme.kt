package com.example.programaficharfeoe.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AzulHidrocaex,
    secondary = AzulOscuro,
    background = Blanco,
    surface = Blanco,
    onPrimary = Blanco,
    onBackground = GrisOscuro,
    onSurface = GrisOscuro
)

@Composable
fun ProgramaFicharFEOETheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}