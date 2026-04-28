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

class FichajeViewModel : ViewModel() {

    private val repo = AppModule.fichajeRepository

    var fichajesLocales by mutableStateOf<List<Fichaje>>(emptyList())
        private set

    var ultimaAccion by mutableStateOf<String?>(null)
        private set

    var estadoActual by mutableStateOf<String?>(null)
        private set

    var haFichadoHoy by mutableStateOf(false)
        private set

    var cargando by mutableStateOf(false)
        private set

    var mensaje by mutableStateOf<String?>(null)

    var accionesDisponibles by mutableStateOf<List<String>>(emptyList())
        private set

    fun limpiarMensaje() {
        mensaje = null
    }

    fun cargarDatos(userId: Int) {
        viewModelScope.launch {
            cargando = true
            try {
                val fichajesResult = repo.getFichajesDelDia(userId)
                val accionesResult = repo.getSiguientesAcciones(userId)

                fichajesResult.onSuccess { fichajes ->
                    fichajesLocales = fichajes
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

                    haFichadoHoy = fichajesLocales.isNotEmpty()
                    ultimaAccion = fichajesLocales.lastOrNull()?.tipo
                }

                accionesResult.onSuccess { response ->
                    estadoActual = response.estado

                    accionesDisponibles =
                        response.accionesTaller +
                                response.accionesObra +
                                response.accionesReparacion
                }

                accionesResult.onFailure {
                    mensaje = it.message ?: "Error al obtener las acciones"
                    Log.e("FICHAJE", "Error al obtener acciones", it)
                }

            } catch (e: Exception) {
                mensaje = "Error al cargar los datos"
                Log.e("FICHAJE", "Error", e)
            } finally {
                cargando = false
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
                    timestamp = System.currentTimeMillis(), // ✅ milisegundos
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

                result.onSuccess {
                    mensaje = "Fichaje registrado correctamente"
                    cargarDatos(userId)
                }

                result.onFailure {
                    mensaje = it.message ?: "Error al fichar"
                }

            } catch (e: Exception) {
                mensaje = "Error inesperado"
            }
        }
    }

}