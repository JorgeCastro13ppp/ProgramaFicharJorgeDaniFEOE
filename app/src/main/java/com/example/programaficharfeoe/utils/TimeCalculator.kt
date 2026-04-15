package com.example.programaficharfeoe.utils

// NORMALIZAR TIMESTAMP
fun normalizarTimestamp(timestamp: Long): Long =
    when {
        timestamp <= 0 -> 0L
        timestamp < 1_000_000_000_000L -> timestamp * 1000
        else -> timestamp
    }