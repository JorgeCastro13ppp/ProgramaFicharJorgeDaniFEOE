package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Vacacion
import com.example.programaficharfeoe.di.AppModule
import kotlinx.coroutines.launch
import com.example.programaficharfeoe.ui.state.VacacionesUiState
import com.example.programaficharfeoe.utils.ApiResult

class VacacionesViewModel : ViewModel() {

    private val repository = AppModule.vacacionesRepository

    var uiState by mutableStateOf(
        VacacionesUiState()
    )
        private set

    fun cargarVacaciones() {
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true,
                error = null
            )

            when (val result = repository.getVacaciones()) {

                is ApiResult.Success -> {

                    uiState = uiState.copy(
                        vacaciones = result.data
                    )
                }

                is ApiResult.Error -> {

                    uiState = uiState.copy(
                        error = result.message
                    )
                }
            }

            uiState = uiState.copy(
                isLoading = false
            )
        }
    }

    fun solicitarVacaciones(fechaInicio: String, fechaFin: String) {
        viewModelScope.launch {
            uiState = uiState.copy(
                solicitudOk = false,
                errorSolicitud = null
            )

            when (
                val result =
                    repository.solicitarVacaciones(
                        fechaInicio,
                        fechaFin
                    )
            ) {

                is ApiResult.Success -> {

                    uiState = uiState.copy(
                        solicitudOk = true
                    )

                    cargarVacaciones()
                }

                is ApiResult.Error -> {

                    uiState = uiState.copy(
                        errorSolicitud = result.message
                    )
                }
            }
        }
    }
}