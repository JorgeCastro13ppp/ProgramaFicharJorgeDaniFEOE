package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.di.AppModule
import kotlinx.coroutines.launch
import com.example.programaficharfeoe.ui.state.DocumentosUiState
import com.example.programaficharfeoe.utils.ApiResult

class DocumentosViewModel : ViewModel() {

    private val repository = AppModule.documentoRepository

    var uiState by mutableStateOf(
        DocumentosUiState()
    )
        private set

    fun cargarDocumentos(tipo: String) {
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true,
                error = null
            )

            when (
                val result =
                    repository.getDocumentos(tipo)
            ) {

                is ApiResult.Success -> {

                    uiState = uiState.copy(
                        documentos = result.data
                    )
                }

                is ApiResult.Error -> {

                    uiState = uiState.copy(
                        error = result.message
                    )
                }
            }

            uiState = uiState.copy(
                isLoading = false
            )
        }
    }
}