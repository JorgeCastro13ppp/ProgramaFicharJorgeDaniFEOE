package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Falta
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.data.remote.RetrofitClient

class FaltasRepository {

    private val api = RetrofitClient.api

    suspend fun getFaltas(): List<Falta>? {
        val response = api.getFaltas()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}