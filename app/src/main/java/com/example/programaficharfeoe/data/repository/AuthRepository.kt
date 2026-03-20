package com.example.programaficharfeoe.data.repository

import kotlinx.coroutines.delay

class AuthRepository {

    suspend fun login(username: String, password: String): Boolean {
        delay(1000) // Simula llamada a API

        return username == "admin" && password == "1234"
    }
}