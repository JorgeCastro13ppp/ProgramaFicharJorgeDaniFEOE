package com.example.programaficharfeoe.utils

// NORMALIZAR TIMESTAMP
fun normalizarTimestamp(timestamp: Long): Long {
    if (timestamp <= 0) return 0L

    return if (timestamp < 1_000_000_000_000L) {
        timestamp * 1000 // Convertir de segundos a milisegundos
    } else {
        timestamp
    }
}