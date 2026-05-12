package com.example.programaficharfeoe.ui.state

import com.example.programaficharfeoe.data.model.Documento

data class DocumentosUiState(

    val documentos: List<Documento> = emptyList(),

    val isLoading: Boolean = false,

    val error: String? = null
)