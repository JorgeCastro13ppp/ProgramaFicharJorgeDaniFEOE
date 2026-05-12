package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Vacacion
import com.example.programaficharfeoe.data.model.VacacionesRequest
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.utils.ApiResult
import com.example.programaficharfeoe.utils.safeApiCall

class VacacionesRepository(private val api: ApiService) {

    suspend fun getVacaciones(): ApiResult<List<Vacacion>> {

        return safeApiCall {

            api.getVacaciones()
        }
    }

    suspend fun solicitarVacaciones(
        fechaInicio: String,
        fechaFin: String
    ): ApiResult<Unit> {

        return safeApiCall {

            api.solicitarVacaciones(

                VacacionesRequest(
                    fechaInicio,
                    fechaFin
                )
            )
        }
    }
}