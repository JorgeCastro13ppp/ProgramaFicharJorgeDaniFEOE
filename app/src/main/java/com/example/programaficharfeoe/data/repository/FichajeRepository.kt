package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.*
import com.example.programaficharfeoe.data.remote.RetrofitInstance

class FichajeRepository {

    private val api = RetrofitInstance.api

    suspend fun getFichajesDelDia(userId: Int) =
        api.getFichajesDelDia(userId)

    suspend fun getEstadoActual(userId: Int) =
        api.getEstadoActual(userId)

    suspend fun getSiguientesAcciones(userId: Int): SiguientesAccionesResponse {
        return api.getSiguientesAcciones(userId)
    }

    suspend fun fichar(request: FichajeEventoRequest) =
        api.fichar(request)
}