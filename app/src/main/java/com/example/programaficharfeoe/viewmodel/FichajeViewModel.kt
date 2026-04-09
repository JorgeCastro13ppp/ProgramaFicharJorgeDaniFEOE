package com.example.programaficharfeoe.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.location.LocationService
import com.example.programaficharfeoe.data.model.FichajeEventoRequest
import com.example.programaficharfeoe.data.repository.FichajeRepository
import kotlinx.coroutines.launch

class FichajeViewModel : ViewModel() {

    private val repo = FichajeRepository()

    var loading by mutableStateOf(false)
    var mensaje by mutableStateOf<String?>(null)

    fun fichar(
        context: Context,
        userId: Int,
        contexto: String,
        accion: String
    ) {

        viewModelScope.launch {

            loading = true

            val locationService = LocationService(context)
            val location = locationService.getLastLocation()

            val lat = location?.first ?: 0.0
            val lng = location?.second ?: 0.0
            val accuracy = location?.third ?: 0.0

            val request = FichajeEventoRequest(
                userId = userId,
                timestamp = System.currentTimeMillis(),
                contexto = contexto,
                accion = accion,
                latitud = lat,
                longitud = lng,
                accuracy = accuracy
            )

            val result = repo.fichar(request)

            loading = false

            result
                .onSuccess {
                    mensaje = it
                    println("SUCCESS FICHAJE: $it")
                }
                .onFailure {
                    mensaje = it.message
                    println("ERROR FICHAJE: ${it.message}")
                }
        }
    }
}