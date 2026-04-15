package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Vacacion
import com.example.programaficharfeoe.di.AppModule
import kotlinx.coroutines.launch

class VacacionesViewModel : ViewModel() {

    private val repository = AppModule.vacacionesRepository

    var vacaciones by mutableStateOf<List<Vacacion>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var solicitudOk by mutableStateOf(false)
        private set

    var errorSolicitud by mutableStateOf<String?>(null)
        private set

    fun cargarVacaciones() {
        viewModelScope.launch {
            isLoading = true
            error = null

            repository.getVacaciones()
                .onSuccess { vacaciones = it }
                .onFailure { error = it.message }

            isLoading = false
        }
    }

    fun solicitarVacaciones(fechaInicio: String, fechaFin: String) {
        viewModelScope.launch {
            solicitudOk = false
            errorSolicitud = null

            repository.solicitarVacaciones(fechaInicio, fechaFin)
                .onSuccess {
                    solicitudOk = true
                    cargarVacaciones()
                }
                .onFailure {
                    errorSolicitud = it.message
                }
        }
    }
}