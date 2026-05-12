package com.example.programaficharfeoe.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.di.AppModule
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import com.example.programaficharfeoe.ui.state.LoginUiState
import com.example.programaficharfeoe.utils.ApiResult

class LoginViewModel : ViewModel() {

    private val repository = AppModule.authRepository

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onUsernameChange(value: String) {

        uiState = uiState.copy(
            username = value
        )
    }

    fun onPasswordChange(value: String) {

        uiState = uiState.copy(
            password = value
        )
    }

    fun limpiarError() {

        uiState = uiState.copy(
            error = null
        )
    }


    fun login() {

        viewModelScope.launch {

            if (
                uiState.username.isBlank() ||
                uiState.password.isBlank()
            ) {

                uiState = uiState.copy(
                    error = "Completa todos los campos"
                )

                return@launch
            }

            uiState = uiState.copy(
                isLoading = true,
                error = null
            )

            when (
                val result = repository.login(
                    uiState.username,
                    uiState.password
                )
            ) {

                is ApiResult.Success -> {

                    val response = result.data

                    SessionManager.saveToken(response.token)

                    SessionManager.saveUsername(
                        uiState.username
                    )

                    SessionManager.saveUserId(
                        response.userId
                    )

                    enviarTokenFCMAlBackend(
                        response.token
                    )

                    uiState = uiState.copy(
                        loginSuccess = true,
                        isLoading = false
                    )
                }

                is ApiResult.Error -> {

                    uiState = uiState.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }


    private fun enviarTokenFCMAlBackend(
        jwt: String
    ) {

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { fcmToken ->

                viewModelScope.launch {

                    when (

                        val result =
                            repository.registrarTokenFCM(
                                jwt,
                                fcmToken
                            )

                    ) {

                        is ApiResult.Success -> {

                            Log.d(
                                "FCM_DEBUG",
                                "Token enviado correctamente"
                            )
                        }

                        is ApiResult.Error -> {

                            Log.e(
                                "FCM_DEBUG",
                                result.message
                            )
                        }
                    }
                }
            }
        }
}