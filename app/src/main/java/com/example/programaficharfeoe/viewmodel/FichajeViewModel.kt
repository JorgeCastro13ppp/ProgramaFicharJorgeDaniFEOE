package com.example.programaficharfeoe.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.location.LocationService
import com.example.programaficharfeoe.data.model.*
import com.example.programaficharfeoe.data.repository.FichajeRepository
import kotlinx.coroutines.launch
import com.example.programaficharfeoe.utils.normalizarTimestamp

class FichajeViewModel : ViewModel() {

    private val repo = FichajeRepository()

    var fichajesLocales by mutableStateOf<List<Fichaje>>(emptyList())
        private set

    var accionesPermitidas by mutableStateOf<List<String>>(emptyList())
        private set

    var ultimaAccion by mutableStateOf<String?>(null)
        private set

    var estadoActual by mutableStateOf<String?>(null)
        private set

    var cargando by mutableStateOf(false)
        private set

    var mensaje by mutableStateOf<String?>(null)

    fun cargarDatos(userId: Int) {
        viewModelScope.launch {
            try {
                cargando = true

                val resultado = repo.getFichajesDelDia(userId)

                fichajesLocales = resultado.map {
                    Fichaje(
                        id = it.id,
                        user_id = it.userId,
                        tipo = it.tipo.uppercase(),
                        fecha_hora = normalizarTimestamp(it.fechaHora)
                    )
                }.sortedBy { it.fecha_hora }

                ultimaAccion = fichajesLocales.lastOrNull()?.tipo

            } catch (e: Exception) {
                mensaje = "Error al cargar los datos"
                e.printStackTrace()
            } finally {
                cargando = false
            }
        }
    }

    fun puedeFichar(accion: String, contextoSeleccionado: String): Boolean {
        val ultimoFichaje = fichajesLocales.lastOrNull() ?: return accion == "ENTRADA"

        val partes = ultimoFichaje.tipo.split("·")
        val ultimaAccion = partes[0].trim().uppercase()
        val ultimoContexto = partes.getOrNull(1)?.trim()?.uppercase() ?: ""

        val accionNormalizada = accion.uppercase()
        val contextoActual = contextoSeleccionado.uppercase()

        return when (ultimaAccion) {

            "ENTRADA" -> {
                when (accionNormalizada) {
                    "SALIDA" -> contextoActual == ultimoContexto
                    "INICIO_DESCANSO" -> contextoActual == ultimoContexto
                    "INICIO_VIAJE" -> contextoActual != "TALLER"
                    else -> false
                }
            }

            "SALIDA" -> accionNormalizada == "ENTRADA"

            "INICIO_VIAJE" -> accionNormalizada == "FIN_VIAJE"

            "FIN_VIAJE" -> accionNormalizada == "ENTRADA"

            "INICIO_DESCANSO" -> accionNormalizada == "FIN_DESCANSO"

            "FIN_DESCANSO" -> {
                when (accionNormalizada) {
                    "SALIDA" -> contextoActual == ultimoContexto
                    "INICIO_VIAJE" -> contextoActual != "TALLER"
                    else -> false
                }
            }

            else -> accionNormalizada == "ENTRADA"
        }
    }

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
                    contexto = contexto,
                    accion = accion,
                    latitud = location?.first ?: 0.0,
                    longitud = location?.second ?: 0.0,
                    accuracy = location?.third ?: 0.0
                )

                repo.fichar(request)
                cargarDatos(userId)

                mensaje = "Fichaje registrado correctamente"

            } catch (e: Exception) {
                mensaje = e.message ?: "Error al fichar"
            }
        }
    }
}