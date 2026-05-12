package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Falta
import com.example.programaficharfeoe.di.AppModule
import kotlinx.coroutines.launch
import com.example.programaficharfeoe.ui.state.FaltasUiState
import com.example.programaficharfeoe.utils.ApiResult

class FaltasViewModel : ViewModel() {

    private val repository = AppModule.faltasRepository

    var uiState by mutableStateOf(
        FaltasUiState()
    )
        private set

    fun cargarFaltas() {
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true,
                error = null
            )

            when (
                val result =
                    repository.getFaltas()
            ) {

                is ApiResult.Success -> {

                    uiState = uiState.copy(
                        faltas = result.data
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
}