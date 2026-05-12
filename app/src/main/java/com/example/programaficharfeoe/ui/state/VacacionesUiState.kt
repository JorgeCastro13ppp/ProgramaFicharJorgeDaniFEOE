package com.example.programaficharfeoe.ui.state

import com.example.programaficharfeoe.data.model.Vacacion

data class VacacionesUiState(

    val vacaciones: List<Vacacion> = emptyList(),

    val isLoading: Boolean = false,

    val error: String? = null,

    val solicitudOk: Boolean = false,

    val errorSolicitud: String? = null
)