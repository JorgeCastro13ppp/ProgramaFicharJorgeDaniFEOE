package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.di.AppModule
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = AppModule.authRepository

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var loginResult by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun login() {
        viewModelScope.launch {
            isLoading = true

            repository.login(username, password)
                .onSuccess {
                    SessionManager.saveToken(it.token)
                    SessionManager.saveUsername(username)
                    SessionManager.saveUserId(it.userId)
                    loginResult = "OK"
                }
                .onFailure {
                    loginResult = "ERROR"
                }

            isLoading = false
        }
    }
}