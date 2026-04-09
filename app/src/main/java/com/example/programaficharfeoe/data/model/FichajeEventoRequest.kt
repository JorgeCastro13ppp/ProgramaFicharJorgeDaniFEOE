package com.example.programaficharfeoe.data.model

data class FichajeEventoRequest(
    val userId: Int,
    val timestamp: Long,
    val contexto: String,
    val accion: String,
    val latitud: Double,
    val longitud: Double,
    val accuracy: Double
)