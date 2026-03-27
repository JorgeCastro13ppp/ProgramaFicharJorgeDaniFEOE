package com.example.programaficharfeoe.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var loginResult by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    fun login() {
        viewModelScope.launch {

            isLoading = true

            val response = repository.login(username, password)

            isLoading = false

            if (response != null) {
                SessionManager.saveToken(response.token)
                SessionManager.saveUsername(username)
                loginResult = "OK"
            } else {
                loginResult = "ERROR"
            }
        }
    }
}