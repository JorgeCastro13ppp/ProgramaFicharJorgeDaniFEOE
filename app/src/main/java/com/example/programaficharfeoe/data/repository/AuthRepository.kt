package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.LoginRequest
import com.example.programaficharfeoe.data.model.LoginResponse
import com.example.programaficharfeoe.data.remote.ApiService

class AuthRepository(private val api: ApiService) {

    suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse> = runCatching {
        api.login(LoginRequest(username, password))
    }
}