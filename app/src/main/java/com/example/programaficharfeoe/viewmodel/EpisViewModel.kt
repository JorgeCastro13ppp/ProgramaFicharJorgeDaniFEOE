package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.data.repository.DocumentoRepository
import kotlinx.coroutines.launch

class EpisViewModel : ViewModel() {

    private val repository = DocumentoRepository()

    var epis by mutableStateOf<List<Documento>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun cargarEpis() {

        viewModelScope.launch {
            isLoading = true
            error = null

            try {
                epis = repository.getEpis() ?: emptyList()
            } catch (e: Exception) {
                error = "Error de conexión"
            }

            isLoading = false
        }
    }
}