package com.example.programaficharfeoe.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.di.AppModule
import kotlinx.coroutines.launch
import com.example.programaficharfeoe.utils.ApiResult
import com.example.programaficharfeoe.ui.state.DashboardUiState

class DashboardViewModel : ViewModel() {

    private val fichajeRepo = AppModule.fichajeRepository
    private val api = AppModule.api

    // ESTADO
    var uiState by mutableStateOf(
        DashboardUiState()
    )
        private set

    // CARGA DASHBOARD
    fun cargarDashboard(userId: Int) {

        viewModelScope.launch {

            uiState = uiState.copy(
                cargando = true,
                error = null
            )

            try {

                // ESTADO ACTUAL
                when (val result = fichajeRepo.getEstadoActual(userId)) {

                    is ApiResult.Success -> {

                        uiState = uiState.copy(
                            estadoActual = result.data.estado
                        )
                    }

                    is ApiResult.Error -> {

                        uiState = uiState.copy(
                            error = result.message
                        )
                    }
                }

                // ACCIONES DISPONIBLES
                when (val result = fichajeRepo.getSiguientesAcciones(userId)) {

                    is ApiResult.Success -> {

                        val response = result.data

                        uiState = uiState.copy(
                            accionesDisponibles =
                                response.accionesTaller +
                                        response.accionesObra +
                                        response.accionesReparacion
                        )
                    }

                    is ApiResult.Error -> {

                        uiState = uiState.copy(
                            error = result.message
                        )
                    }
                }

                // FICHAJES DE HOY
                when (val result = fichajeRepo.getFichajesDelDia(userId)) {

                    is ApiResult.Success -> {

                        uiState = uiState.copy(
                            fichajesHoy =
                                result.data.sortedBy { it.fechaHora }
                        )
                    }

                    is ApiResult.Error -> {

                        uiState = uiState.copy(
                            error = result.message
                        )
                    }
                }

                // HORAS HOY (MILISEGUNDOS)
                try {
                    val response = api.getHorasHoy(userId)

                    uiState = uiState.copy(
                        horasHoyMs = response.tiempo
                    )

                } catch (e: Exception) {

                    Log.e("DASHBOARD", "Error horas", e)

                    uiState = uiState.copy(
                        horasHoyMs = null
                    )
                }

                try {
                    val response = api.getVacacionesResumen()

                    Log.d("VACACIONES", response.toString())

                    uiState = uiState.copy(

                        diasVacacionesRestantes =
                            response.diasTotalesRestantes,

                        diasLibresRestantes =
                            response.diasLibresRestantes,

                        diasNavidadRestantes =
                            response.diasNavidadRestantes
                    )

                } catch (e: Exception) {
                    Log.e("VACACIONES", "ERROR", e)
                }

            } catch (e: Exception) {

                uiState = uiState.copy(
                    error = e.message ?: "Error cargando dashboard"
                )
            } finally {
                uiState = uiState.copy(
                    cargando = false
                )
            }
        }
    }
}