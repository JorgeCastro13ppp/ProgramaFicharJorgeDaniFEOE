package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.data.repository.DocumentoRepository
import kotlinx.coroutines.launch

class NominasViewModel : ViewModel() {

    private val repository = DocumentoRepository()

    var nominas by mutableStateOf<List<Documento>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun cargarNominas() {

        viewModelScope.launch {
            isLoading = true
            error = null

            try {
                nominas = repository.getNominas()  ?: emptyList()
            } catch (e: Exception) {
                error = "Error al cargar nóminas"
            }

            isLoading = false
        }
    }
}