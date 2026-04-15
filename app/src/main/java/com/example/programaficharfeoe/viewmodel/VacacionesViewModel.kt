package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Vacacion
import com.example.programaficharfeoe.data.repository.VacacionesRepository
import kotlinx.coroutines.launch

class VacacionesViewModel : ViewModel() {

    private val repository = VacacionesRepository()

    // LISTADO
    var vacaciones = mutableStateOf<List<Vacacion>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    var error = mutableStateOf<String?>(null)
        private set

    // SOLICITUD
    var solicitudOk = mutableStateOf(false)
        private set

    var errorSolicitud = mutableStateOf<String?>(null)
        private set

    // CARGAR VACACIONES
    fun cargarVacaciones() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null

            try {
                val result = repository.getVacaciones()

                if (result != null) {
                    vacaciones.value = result
                } else {
                    error.value = "Error al cargar vacaciones"
                }

            } catch (e: Exception) {
                error.value = e.message
            }

            isLoading.value = false
        }
    }

    // SOLICITAR VACACIONES
    fun solicitarVacaciones(fechaInicio: String, fechaFin: String) {
        viewModelScope.launch {

            solicitudOk.value = false
            errorSolicitud.value = null

            try {
                val ok = repository.solicitarVacaciones(fechaInicio, fechaFin)

                if (ok) {
                    solicitudOk.value = true
                    cargarVacaciones() // refrescar lista
                } else {
                    errorSolicitud.value = "Error al solicitar vacaciones"
                }

            } catch (e: Exception) {
                errorSolicitud.value = e.message
            }
        }
    }
}