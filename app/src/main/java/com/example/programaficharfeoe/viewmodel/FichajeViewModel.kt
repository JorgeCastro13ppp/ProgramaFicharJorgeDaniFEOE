package com.example.programaficharfeoe.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.location.LocationService
import com.example.programaficharfeoe.data.model.*
import com.example.programaficharfeoe.di.AppModule
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

    var accionesTaller by mutableStateOf<List<String>>(emptyList())
        private set

    var accionesObra by mutableStateOf<List<String>>(emptyList())
        private set

    var accionesReparacion by mutableStateOf<List<String>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)

    fun limpiarMensaje() {
        mensaje = null
    }

    fun cargarDatos(userId: Int) {
        viewModelScope.launch {
            cargando = true
            try {
                val fichajesResult = repo.getFichajesDelDia(userId)
                val estadoResult = repo.getEstadoActual(userId)
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

                estadoResult.onSuccess {
                    estadoActual = it.estado
                }

                accionesResult.onSuccess { response ->
                    estadoActual = response.estado
                    accionesTaller = response.accionesTaller
                    accionesObra = response.accionesObra
                    accionesReparacion = response.accionesReparacion
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

    fun puedeFichar(
        accion: String,
        contextoSeleccionado: String
    ): Boolean {
        val accionBase = accion.uppercase()
        val contexto = contextoSeleccionado.uppercase()

        val accionBackend = when (accionBase) {
            "ENTRADA" -> "ENTRADA_$contexto"
            "SALIDA" -> "SALIDA_$contexto"
            "INICIO_VIAJE" -> "INICIO_VIAJE_$contexto"
            "FIN_VIAJE" -> "FIN_VIAJE_$contexto"
            "INICIO_DESCANSO" -> "INICIO_DESCANSO_$contexto"
            "FIN_DESCANSO" -> "FIN_DESCANSO_$contexto"
            else -> return false
        }

        val accionesContexto = when (contexto) {
            "TALLER" -> accionesTaller
            "OBRA" -> accionesObra
            "REPARACION" -> accionesReparacion
            else -> emptyList()
        }

        return accionesContexto.contains(accionBackend)
    }

    fun fichar(
        context: Context,
        userId: Int,
        contexto: String,
        accion: String
    ) {
        viewModelScope.launch {
            try {
                val location = LocationService(context).getLastLocation()

                val request = FichajeEventoRequest(
                    userId = userId,
                    timestamp = System.currentTimeMillis(),
                    contexto = contexto.uppercase(),
                    accion = accion.uppercase(),
                    latitud = location?.latitude ?: 0.0,
                    longitud = location?.longitude ?: 0.0,
                    accuracy = location?.accuracy ?: 0.0
                )

                repo.fichar(request)
                cargarDatos(userId)
                mensaje = "Fichaje registrado correctamente"

            } catch (e: Exception) {
                mensaje = e.message ?: "Error al fichar"
                Log.e("FICHAJE", "Error al fichar", e)
            }
        }
    }

    fun obtenerAccionesDisponibles(): List<Pair<String, String>> {
        val acciones = mutableListOf<Pair<String, String>>()

        fun procesar(lista: List<String>, contexto: String) {
            lista.forEach { accionCompleta ->
                val accion = accionCompleta.substringBeforeLast("_")
                acciones.add(accion to contexto)
            }
        }

        procesar(accionesTaller, "TALLER")
        procesar(accionesObra, "OBRA")
        procesar(accionesReparacion, "REPARACION")

        return acciones
    }
}