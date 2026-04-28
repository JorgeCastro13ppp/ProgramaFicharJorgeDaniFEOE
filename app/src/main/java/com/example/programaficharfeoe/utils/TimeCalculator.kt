package com.example.programaficharfeoe.utils

import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

fun normalizarTimestamp(timestamp: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return formato.format(Date(timestamp))
}

fun formatMillis(ms: Long?): String {
    if (ms == null) return "--"

    val totalMin = ms / 60000
    val horas = totalMin / 60
    val minutos = totalMin % 60

    return "${horas}h ${minutos}min"
}

fun getEstadoColor(estado: String?): Color {
    return when (estado) {
        "EN_TALLER", "EN_OBRA", "EN_REPARACION" -> Color(0xFF22C55E) // verde
        "DESCANSO_TALLER", "DESCANSO_OBRA" -> Color(0xFFF59E0B) // naranja
        "FUERA" -> Color(0xFFEF4444) // rojo
        else -> Color(0xFF3B82F6) // azul fallback
    }
}