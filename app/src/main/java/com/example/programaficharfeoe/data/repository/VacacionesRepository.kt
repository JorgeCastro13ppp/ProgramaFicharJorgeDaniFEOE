package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Vacacion
import com.example.programaficharfeoe.data.model.VacacionesRequest
import com.example.programaficharfeoe.data.model.VacacionesResumenResponse
import com.example.programaficharfeoe.data.remote.ApiService

class VacacionesRepository(private val api: ApiService) {

    suspend fun getVacaciones(): Result<List<Vacacion>> =
        runCatching {
            api.getVacaciones()
        }

    suspend fun solicitarVacaciones(
        fechaInicio: String,
        fechaFin: String
    ): Result<Unit> =
        runCatching {
            api.solicitarVacaciones(
                VacacionesRequest(fechaInicio, fechaFin)
            )
        }

    suspend fun getVacacionesResumen(userId: Int): Result<VacacionesResumenResponse> =
        runCatching {
            api.getVacacionesResumen()
        }
}