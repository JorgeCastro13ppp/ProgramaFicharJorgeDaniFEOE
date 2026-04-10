package com.example.programaficharfeoe.data.model

data class EstadoActualResponse(
    val estado: String,
    val contexto: String?,
    val accion: String?,
    val timestamp: Long?
)