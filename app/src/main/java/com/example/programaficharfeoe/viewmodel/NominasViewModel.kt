package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.data.repository.NominasRepository
import kotlinx.coroutines.launch

class NominasViewModel : ViewModel() {

    private val repository = NominasRepository()

    var nominas = mutableStateOf<List<Documento>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    var error = mutableStateOf<String?>(null)
        private set

    fun cargarNominas() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null

            try {
                val result = repository.getNominas()

                if (result != null) {
                    nominas.value = result
                } else {
                    error.value = "Error al cargar nóminas"
                }

            } catch (e: Exception) {
                error.value = e.message
            }

            isLoading.value = false
        }
    }
}