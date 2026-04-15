package com.example.programaficharfeoe.data.model

data class SiguientesAccionesResponse(
    val estado: String,
    val accionesTaller: List<String>,
    val accionesObra: List<String>,
    val accionesReparacion: List<String>
)