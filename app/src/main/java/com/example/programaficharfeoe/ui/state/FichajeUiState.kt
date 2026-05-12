package com.example.programaficharfeoe.ui.state

import com.example.programaficharfeoe.data.model.Fichaje

data class FichajeUiState(

    val fichajesLocales: List<Fichaje> = emptyList(),

    val ultimaAccion: String? = null,

    val estadoActual: String? = null,

    val haFichadoHoy: Boolean = false,

    val cargando: Boolean = false,

    val mensaje: String? = null,

    val accionesDisponibles: List<String> = emptyList()
)