package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.*
import com.example.programaficharfeoe.data.remote.FichajeApiService
import com.example.programaficharfeoe.data.remote.RetrofitInstance
import retrofit2.http.*


class FichajeRepository {

    private val api = RetrofitInstance.api

    suspend fun fichar(request: FichajeEventoRequest): Result<FichajeEventoResponse> {
        return try {
            Result.success(api.fichar(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFichajesDelDia(userId: Int): List<FichajeResponse> {
        return api.getFichajesDelDia(userId)
    }

    suspend fun getUltimoFichaje(userId: Int): FichajeResponse {
        return api.getUltimoFichaje(userId)
    }

    suspend fun getEstadoActual(userId: Int): EstadoActualResponse {
        return api.getEstadoActual(userId)
    }

    suspend fun getSiguientesAcciones(userId: Int): SiguientesAccionesResponse {
        return api.getSiguientesAcciones(userId)
    }
}