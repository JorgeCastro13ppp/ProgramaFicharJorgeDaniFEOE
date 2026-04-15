package com.example.programaficharfeoe.data.model

data class LoginResponse(
    val token: String,
    val userId: Int,
    val username: String? = null
)