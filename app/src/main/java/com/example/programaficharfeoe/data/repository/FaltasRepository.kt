package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Falta
import com.example.programaficharfeoe.data.remote.ApiService

class FaltasRepository(private val api: ApiService) {

    suspend fun getFaltas(): Result<List<Falta>> =
        runCatching {
            api.getFaltas()
        }
}