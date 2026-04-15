package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.*
import com.example.programaficharfeoe.data.remote.ApiService

class FichajeRepository(private val api: ApiService) {

    suspend fun getFichajesDelDia(userId: Int): Result<List<Fichaje>> =
        runCatching {
            api.getFichajesDelDia(userId)
        }

    suspend fun getEstadoActual(userId: Int): Result<EstadoActualResponse> =
        runCatching {
            api.getEstadoActual(userId)
        }

    suspend fun getSiguientesAcciones(userId: Int): Result<SiguientesAccionesResponse> =
        runCatching {
            api.getSiguientesAcciones(userId)
        }

    suspend fun fichar(request: FichajeEventoRequest): Result<FichajeEventoResponse> =
        runCatching {
            api.ficharEvento(request)
        }
}