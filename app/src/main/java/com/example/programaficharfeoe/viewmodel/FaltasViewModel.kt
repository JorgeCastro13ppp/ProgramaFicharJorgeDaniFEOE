package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Falta
import com.example.programaficharfeoe.di.AppModule
import kotlinx.coroutines.launch

class FaltasViewModel : ViewModel() {

    private val repository = AppModule.faltasRepository

    var faltas by mutableStateOf<List<Falta>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun cargarFaltas() {
        viewModelScope.launch {
            isLoading = true
            error = null

            repository.getFaltas()
                .onSuccess { faltas = it }
                .onFailure { error = it.message }

            isLoading = false
        }
    }
}