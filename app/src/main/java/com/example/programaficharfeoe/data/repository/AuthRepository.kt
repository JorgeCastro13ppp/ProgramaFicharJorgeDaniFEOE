package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.LoginRequest
import com.example.programaficharfeoe.data.model.LoginResponse
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.data.remote.RetrofitClient

class AuthRepository {

    private val api = RetrofitClient.api

    suspend fun login(username: String, password: String): LoginResponse? {
        val response = api.login(LoginRequest(username, password))
        return if (response.isSuccessful) response.body() else null
    }
}