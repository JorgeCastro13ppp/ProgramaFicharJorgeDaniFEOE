package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.di.AppModule
import kotlinx.coroutines.launch

class DocumentosViewModel : ViewModel() {

    private val repository = AppModule.documentoRepository

    var documentos by mutableStateOf<List<Documento>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun cargarDocumentos(tipo: String) {
        viewModelScope.launch {
            isLoading = true
            error = null

            repository.getDocumentos(tipo)
                .onSuccess { documentos = it }
                .onFailure { error = it.message }

            isLoading = false
        }
    }
}