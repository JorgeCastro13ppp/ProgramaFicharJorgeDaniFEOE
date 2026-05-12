package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.*
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.utils.ApiResult
import com.example.programaficharfeoe.utils.safeApiCall

class FichajeRepository(
    private val api: ApiService
) {

    suspend fun getFichajesDelDia(
        userId: Int
    ): ApiResult<List<Fichaje>> {

        return safeApiCall {

            api.getFichajesDelDia(userId)
        }
    }

    suspend fun getEstadoActual(
        userId: Int
    ): ApiResult<EstadoActualResponse> {

        return safeApiCall {

            api.getEstadoActual(userId)
        }
    }

    suspend fun getSiguientesAcciones(
        userId: Int
    ): ApiResult<SiguientesAccionesResponse> {

        return safeApiCall {

            api.getSiguientesAcciones(userId)
        }
    }

    suspend fun fichar(
        request: FichajeEventoRequest
    ): ApiResult<FichajeEventoResponse> {

        return safeApiCall {

            api.ficharEvento(request)
        }
    }
}