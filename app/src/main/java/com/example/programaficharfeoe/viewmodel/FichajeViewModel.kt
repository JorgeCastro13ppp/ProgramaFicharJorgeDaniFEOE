package com.example.programaficharfeoe.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.location.LocationService
import com.example.programaficharfeoe.data.model.*
import com.example.programaficharfeoe.di.AppModule
import com.google.gson.Gson
import kotlinx.coroutines.launch
import com.example.programaficharfeoe.ui.state.FichajeUiState
import com.example.programaficharfeoe.utils.ApiResult

class FichajeViewModel : ViewModel() {

    private val repo = AppModule.fichajeRepository

    var uiState by mutableStateOf(FichajeUiState())
        private set

    fun limpiarMensaje() {

        uiState = uiState.copy(
            mensaje = null
        )
    }

    fun cargarDatos(userId: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(
                cargando = true
            )
            try {
                val fichajesResult = repo.getFichajesDelDia(userId)
                val accionesResult = repo.getSiguientesAcciones(userId)

                when (fichajesResult) {

                    is ApiResult.Success -> {

                        val fichajes = fichajesResult.data

                        val fichajesTransformados = fichajes
                            .sortedBy { it.fechaHora }
                            .map {
                                Fichaje(
                                    id = it.id,
                                    userId = it.userId,
                                    username = it.username,
                                    fechaHora = it.fechaHora,
                                    tipo = it.tipo.uppercase()
                                )
                            }

                        uiState = uiState.copy(
                            fichajesLocales = fichajesTransformados,
                            haFichadoHoy = fichajesTransformados.isNotEmpty(),
                            ultimaAccion = fichajesTransformados.lastOrNull()?.tipo
                        )
                    }

                    is ApiResult.Error -> {

                        uiState = uiState.copy(
                            mensaje = fichajesResult.message
                        )
                    }
                }

                when (accionesResult) {

                    is ApiResult.Success -> {

                        val response = accionesResult.data

                        uiState = uiState.copy(
                            estadoActual = response.estado,
                            accionesDisponibles =
                                response.accionesTaller +
                                        response.accionesObra +
                                        response.accionesReparacion
                        )
                    }

                    is ApiResult.Error -> {

                        uiState = uiState.copy(
                            mensaje = accionesResult.message
                        )

                        Log.e(
                            "FICHAJE",
                            "Error al obtener acciones"
                        )
                    }
                }

            } catch (e: Exception) {
                uiState = uiState.copy(
                    mensaje = "Error al cargar los datos"
                )
                Log.e("FICHAJE", "Error", e)
            } finally {
                uiState = uiState.copy(
                    cargando = false
                )
            }
        }
    }

    fun fichar(
        context: Context,
        userId: Int,
        accionCompleta: String
    ) {
        viewModelScope.launch {
            try {

                val location = LocationService(context).getLastLocation()

                val accionBase = accionCompleta.substringBeforeLast("_")
                val contexto = accionCompleta.substringAfterLast("_")

                val request = FichajeEventoRequest(
                    userId = userId,
                    timestamp = System.currentTimeMillis(), // milisegundos
                    contexto = contexto,                    // "TALLER"
                    accion = accionBase,                    // "ENTRADA"
                    latitud = location?.latitude ?: 0.0,
                    longitud = location?.longitude ?: 0.0,
                    accuracy = (location?.accuracy ?: 0f).toDouble()
                )

                Log.d("FICHAJE", "REQUEST JSON:")
                Log.d("FICHAJE", request.toString())
                Log.d("FICHAJE_JSON", Gson().toJson(request))

                val result = repo.fichar(request)

                when (result) {

                    is ApiResult.Success -> {

                        uiState = uiState.copy(
                            mensaje = "Fichaje registrado correctamente"
                        )

                        cargarDatos(userId)
                    }

                    is ApiResult.Error -> {

                        uiState = uiState.copy(
                            mensaje = result.message
                        )
                    }
                }

            } catch (e: Exception) {
                uiState = uiState.copy(
                    mensaje = "Error inesperado"
                )
            }
        }
    }

}