package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.repository.FichajeRepository
import kotlinx.coroutines.launch

class FichajeViewModel : ViewModel() {

    private val repository = FichajeRepository()

    var tipoActual by mutableStateOf("entrada")
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        obtenerTipoAutomatico()
    }

    private fun obtenerTipoAutomatico() {
        viewModelScope.launch {
            isLoading = true

            val ultimo = repository.obtenerUltimoFichaje()

            tipoActual = if (ultimo == "entrada") {
                "salida"
            } else {
                "entrada"
            }

            isLoading = false
        }
    }

    fun fichar(qr: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val ok = repository.fichar(qr, tipoActual)

            if (ok) {
                // 🔥 actualizar tipo después de fichar
                tipoActual = if (tipoActual == "entrada") "salida" else "entrada"
            }

            onResult(ok)
        }
    }
}