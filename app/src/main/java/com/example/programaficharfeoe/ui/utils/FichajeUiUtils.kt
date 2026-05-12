package com.example.programaficharfeoe.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

fun obtenerColorFichaje(tipo: String): Color {

    return when {

        tipo.startsWith("ENTRADA") ->
            Color(0xFF22C55E)

        tipo.startsWith("SALIDA") ->
            Color(0xFFEF4444)

        tipo.contains("DESCANSO") ->
            Color(0xFFF59E0B)

        tipo.contains("VIAJE") ->
            Color(0xFF3B82F6)

        else ->
            Color(0xFF2563EB)
    }
}

fun obtenerIconoFichaje(tipo: String): ImageVector {

    return when {

        tipo.startsWith("ENTRADA") ->
            Icons.Default.Login

        tipo.startsWith("SALIDA") ->
            Icons.Default.Logout

        tipo.contains("VIAJE") ->
            Icons.Default.DirectionsCar

        else ->
            Icons.Default.AccessTime
    }
}