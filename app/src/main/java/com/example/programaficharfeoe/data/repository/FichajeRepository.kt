package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Fichaje
import com.example.programaficharfeoe.data.model.FichajeEventoRequest
import com.example.programaficharfeoe.data.model.FichajeRequest
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.data.remote.RetrofitClient

class FichajeRepository {

    suspend fun fichar(request: FichajeEventoRequest): Result<String> {
        return try {
            val response = RetrofitClient.api.fichar(request)

            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "OK")
            } else {
                val error = response.errorBody()?.string() ?: "Error"
                Result.failure(Exception(error))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}