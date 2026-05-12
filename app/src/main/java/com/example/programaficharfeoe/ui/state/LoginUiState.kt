package com.example.programaficharfeoe.ui.state

data class LoginUiState(

    val username: String = "",

    val password: String = "",

    val isLoading: Boolean = false,

    val error: String? = null,

    val loginSuccess: Boolean = false
)