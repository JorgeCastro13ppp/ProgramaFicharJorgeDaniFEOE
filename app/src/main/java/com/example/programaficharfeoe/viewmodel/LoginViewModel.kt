package com.example.programaficharfeoe.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.data.remote.RetrofitClient
import com.example.programaficharfeoe.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val context: Context,
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val sessionManager = SessionManager(context)

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var loginResult by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun login() {
        viewModelScope.launch {
            isLoading = true

            val token = repository.login(username, password)

            if (token != null) {

                // Guardar token
                sessionManager.saveToken(token)

                // Pasarlo a Retrofit
                RetrofitClient.setToken(token)

                loginResult = "OK"
            } else {
                loginResult = "ERROR"
            }

            isLoading = false
        }
    }

    fun resetLoginState() {
        loginResult = null
    }
}