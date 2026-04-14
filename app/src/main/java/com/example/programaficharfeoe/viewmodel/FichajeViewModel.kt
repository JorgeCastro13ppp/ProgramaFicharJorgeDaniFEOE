package com.example.programaficharfeoe.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.location.LocationService
import com.example.programaficharfeoe.data.model.Fichaje
import com.example.programaficharfeoe.data.model.FichajeEventoRequest
import com.example.programaficharfeoe.data.repository.FichajeRepository
import com.example.programaficharfeoe.utils.normalizarTimestamp
import kotlinx.coroutines.launch

class FichajeViewModel : ViewModel() {

    private val repo = FichajeRepository()

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

    // Acciones permitidas por contexto
    var accionesTaller by mutableStateOf<List<String>>(emptyList())
        private set

    var accionesObra by mutableStateOf<List<String>>(emptyList())
        private set

    var accionesReparacion by mutableStateOf<List<String>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)

    // Carga los datos desde el backend
    fun cargarDatos(userId: Int) {
        viewModelScope.launch {
            try {
                cargando = true

                val fichajes = repo.getFichajesDelDia(userId)
                val estado = repo.getEstadoActual(userId)
                val siguientes = repo.getSiguientesAcciones(userId)

                fichajesLocales = fichajes.map {
                    Fichaje(
                        id = it.id,
                        user_id = it.userId,
                        tipo = it.tipo.uppercase(),
                        fecha_hora = normalizarTimestamp(it.fechaHora)
                    )
                }.sortedBy { it.fecha_hora }

                haFichadoHoy = fichajesLocales.isNotEmpty()
                ultimaAccion = fichajesLocales.lastOrNull()?.tipo
                estadoActual = estado.estado

                // Acciones por contexto
                accionesTaller = siguientes.accionesTaller
                accionesObra = siguientes.accionesObra
                accionesReparacion = siguientes.accionesReparacion

                Log.d("FICHAJE", "Ha fichado hoy: $haFichadoHoy")
                Log.d("FICHAJE", "Estado actual: $estadoActual")
                Log.d("FICHAJE", "Acciones Taller: $accionesTaller")
                Log.d("FICHAJE", "Acciones Obra: $accionesObra")
                Log.d("FICHAJE", "Acciones Reparación: $accionesReparacion")

            } catch (e: Exception) {
                mensaje = "Error al cargar los datos"
                Log.e("FICHAJE", "Error al cargar datos", e)
            } finally {
                cargando = false
            }
        }
    }

    // Determina si una acción está permitida según el backend

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

    // Registra un fichaje

    fun fichar(
        context: Context,
        userId: Int,
        contexto: String,
        accion: String
    ) {
        viewModelScope.launch {
            try {
                val locationService = LocationService(context)
                val location = locationService.getLastLocation()

                val request = FichajeEventoRequest(
                    userId = userId,
                    timestamp = System.currentTimeMillis(),
                    contexto = contexto.uppercase(),
                    accion = accion.uppercase(),
                    latitud = location?.first ?: 0.0,
                    longitud = location?.second ?: 0.0,
                    accuracy = location?.third ?: 0.0
                )

                Log.d(
                    "FICHAJE",
                    "Enviando: ${accion.uppercase()}_${contexto.uppercase()}"
                )

                repo.fichar(request)

                // Recargar datos tras el fichaje
                cargarDatos(userId)

                mensaje = "Fichaje registrado correctamente"

            } catch (e: Exception) {
                mensaje = e.message ?: "Error al fichar"
                Log.e("FICHAJE", "Error al fichar", e)
            }
        }
    }
}