package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var loginResult by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun login() {
        viewModelScope.launch {
            isLoading = true

            val success = repository.login(username, password)

            loginResult = if (success) "success" else "error"

            isLoading = false
        }
    }

    fun resetLoginState() {
        loginResult = null
    }
}