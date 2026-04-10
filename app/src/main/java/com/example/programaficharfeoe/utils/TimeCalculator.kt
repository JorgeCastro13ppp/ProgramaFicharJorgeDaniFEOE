package com.example.programaficharfeoe.utils

import com.example.programaficharfeoe.data.model.Fichaje

data class Tiempos(
    val trabajo: Long = 0L,
    val viaje: Long = 0L,
    val descanso: Long = 0L
)

fun calcularTiempos(fichajes: List<Fichaje>): Tiempos {

    var trabajo = 0L
    var viaje = 0L
    var descanso = 0L

    var inicioTrabajo: Long? = null
    var inicioViaje: Long? = null
    var inicioDescanso: Long? = null

    fichajes
        .sortedBy { normalizarTimestamp(it.fecha_hora) }
        .forEach { fichaje ->

            val tiempo = normalizarTimestamp(fichaje.fecha_hora)

            val tipo = fichaje.tipo
                .substringBefore("·")
                .trim()
                .uppercase()

            when (tipo) {

                // 🔄 Nueva jornada
                "ENTRADA" -> {
                    trabajo = 0L
                    viaje = 0L
                    descanso = 0L

                    inicioTrabajo = tiempo
                    inicioViaje = null
                    inicioDescanso = null
                }

                // 🚪 Fin de la jornada
                "SALIDA" -> {
                    inicioTrabajo?.let {
                        trabajo += tiempo - it
                        inicioTrabajo = null
                    }
                    inicioViaje = null
                    inicioDescanso = null
                }

                // 🚗 Inicio de viaje
                "INICIO_VIAJE" -> {
                    inicioTrabajo?.let {
                        trabajo += tiempo - it
                        inicioTrabajo = null
                    }
                    inicioViaje = tiempo
                }

                // 🛬 Fin de viaje
                "FIN_VIAJE" -> {
                    inicioViaje?.let {
                        viaje += tiempo - it
                        inicioViaje = null
                    }
                    inicioTrabajo = tiempo
                }

                // ☕ Inicio de descanso
                "INICIO_DESCANSO" -> {
                    inicioTrabajo?.let {
                        trabajo += tiempo - it
                        inicioTrabajo = null
                    }
                    inicioDescanso = tiempo
                }

                // ▶️ Fin de descanso
                "FIN_DESCANSO" -> {
                    inicioDescanso?.let {
                        descanso += tiempo - it
                        inicioDescanso = null
                    }
                    inicioTrabajo = tiempo
                }
            }
        }

    return Tiempos(
        trabajo = trabajo,
        viaje = viaje,
        descanso = descanso
    )
}

// 🔥 FORMATEO DE TIEMPO
fun formatearTiempo(ms: Long): String {
    if (ms <= 0) return "00:00:00"

    val totalSegundos = ms / 1000
    val horas = totalSegundos / 3600
    val minutos = (totalSegundos % 3600) / 60
    val segundos = totalSegundos % 60

    return "%02d:%02d:%02d".format(horas, minutos, segundos)
}

// 🔧 NORMALIZAR TIMESTAMP
fun normalizarTimestamp(timestamp: Long): Long {
    if (timestamp <= 0) return 0L

    return if (timestamp < 1_000_000_000_000L) {
        timestamp * 1000 // Convertir de segundos a milisegundos
    } else {
        timestamp
    }
}