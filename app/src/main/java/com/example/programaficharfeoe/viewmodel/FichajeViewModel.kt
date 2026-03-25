package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.repository.FichajeRepository
import kotlinx.coroutines.launch

class FichajeViewModel(
    private val repository: FichajeRepository = FichajeRepository()
) : ViewModel() {

    var tipoDisponible by mutableStateOf<String?>(null)
    var mensaje by mutableStateOf<String?>(null)

    init {
        cargarTipoDisponible()
    }

    fun cargarTipoDisponible() {
        viewModelScope.launch {
            val ultimo = repository.obtenerUltimoFichaje()

            tipoDisponible = when (ultimo) {
                "entrada" -> "salida"
                "salida" -> "entrada"
                else -> "entrada"
            }
        }
    }

    fun fichar(token: String, tipo: String) {
        viewModelScope.launch {

            val ok = repository.fichar(token, tipo)

            if (ok) {
                mensaje = "Fichaje de $tipo realizado correctamente"
                cargarTipoDisponible()
            } else {
                mensaje = "Error al fichar"
            }
        }
    }

    fun resetMensaje() {
        mensaje = null
    }
}