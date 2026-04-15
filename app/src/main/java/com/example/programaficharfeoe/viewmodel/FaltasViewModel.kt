package com.example.programaficharfeoe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Falta
import com.example.programaficharfeoe.data.repository.FaltasRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class FaltasViewModel : ViewModel() {

    private val repository = FaltasRepository()

    var faltas by mutableStateOf<List<Falta>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun cargarFaltas() {
        viewModelScope.launch {
            isLoading = true
            val result = repository.getFaltas()
            if (result != null) {
                faltas = result
            }
            isLoading = false
        }
    }
}