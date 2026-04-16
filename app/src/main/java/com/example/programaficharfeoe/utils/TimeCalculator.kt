package com.example.programaficharfeoe.utils

import java.text.SimpleDateFormat
import java.util.*

fun normalizarTimestamp(timestamp: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return formato.format(Date(timestamp))
}