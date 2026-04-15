package com.example.programaficharfeoe.data.model

data class SiguientesAccionesResponse(
    val estado: String,
    val accionesTaller: List<String> = emptyList(),
    val accionesObra: List<String> = emptyList(),
    val accionesReparacion: List<String> = emptyList()
)