package com.example.programaficharfeoe.ui.state

import com.example.programaficharfeoe.data.model.Fichaje

data class DashboardUiState(

    val estadoActual: String? = null,

    val accionesDisponibles: List<String> = emptyList(),

    val fichajesHoy: List<Fichaje> = emptyList(),

    val horasHoyMs: Long? = null,

    val diasVacacionesRestantes: Int? = null,

    val diasLibresRestantes: Int? = null,

    val diasNavidadRestantes: Int? = null,

    val cargando: Boolean = false,

    val error: String? = null
)