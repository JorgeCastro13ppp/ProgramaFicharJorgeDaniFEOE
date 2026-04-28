package com.example.programaficharfeoe.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Fichaje
import com.example.programaficharfeoe.di.AppModule
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val fichajeRepo = AppModule.fichajeRepository
    private val api = AppModule.api

    // ESTADO
    var estadoActual by mutableStateOf<String?>(null)
        private set

    var accionesDisponibles by mutableStateOf<List<String>>(emptyList())
        private set

    var fichajesHoy by mutableStateOf<List<Fichaje>>(emptyList())
        private set

    var horasHoyMs by mutableStateOf<Long?>(null)
        private set

    var diasVacacionesRestantes by mutableStateOf<Int?>(null)

    var diasLibresRestantes by mutableStateOf<Int?>(null)
        private set

    var diasNavidadRestantes by mutableStateOf<Int?>(null)
        private set

    var cargando by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    // CARGA DASHBOARD
    fun cargarDashboard(userId: Int) {

        viewModelScope.launch {

            cargando = true
            error = null

            try {

                // ESTADO ACTUAL
                fichajeRepo.getEstadoActual(userId)
                    .onSuccess {
                        estadoActual = it.estado
                    }
                    .onFailure {
                        error = it.message
                    }

                // ACCIONES DISPONIBLES
                fichajeRepo.getSiguientesAcciones(userId)
                    .onSuccess { response ->

                        accionesDisponibles =
                            (response.accionesTaller +
                                    response.accionesObra +
                                    response.accionesReparacion)
                    }
                    .onFailure {
                        error = it.message
                    }

                // FICHAJES DE HOY
                fichajeRepo.getFichajesDelDia(userId)
                    .onSuccess { lista ->

                        fichajesHoy =
                            lista.sortedBy { it.fechaHora }
                    }
                    .onFailure {
                        error = it.message
                    }

                // HORAS HOY (MILISEGUNDOS)
                try {
                    val response = api.getHorasHoy(userId)

                    horasHoyMs = response.tiempo

                } catch (e: Exception) {

                    Log.e("DASHBOARD", "Error horas", e)

                    horasHoyMs = null
                }

                try {
                    val response = api.getVacacionesResumen()

                    Log.d("VACACIONES", response.toString())

                    diasVacacionesRestantes = response.diasTotalesRestantes
                    diasLibresRestantes = response.diasLibresRestantes
                    diasNavidadRestantes = response.diasNavidadRestantes

                } catch (e: Exception) {
                    Log.e("VACACIONES", "ERROR", e)
                }

            } catch (e: Exception) {

                error = e.message ?: "Error cargando dashboard"

            } finally {
                cargando = false
            }
        }
    }
}