package com.example.programaficharfeoe.ui.state

import com.example.programaficharfeoe.data.model.Falta

data class FaltasUiState(

    val faltas: List<Falta> = emptyList(),

    val isLoading: Boolean = false,

    val error: String? = null
)