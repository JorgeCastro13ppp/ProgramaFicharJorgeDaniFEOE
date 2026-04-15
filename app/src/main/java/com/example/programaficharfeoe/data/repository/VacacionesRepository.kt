package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Vacacion
import com.example.programaficharfeoe.data.model.VacacionesRequest
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.data.remote.RetrofitClient

class VacacionesRepository {

    private val api = RetrofitClient.api

    // 🔹 Obtener vacaciones
    suspend fun getVacaciones(): List<Vacacion>? {
        val response = api.getVacaciones()

        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    // 🔹 Solicitar vacaciones
    suspend fun solicitarVacaciones(
        fechaInicio: String,
        fechaFin: String
    ): Boolean {

        val response = api.solicitarVacaciones(
            VacacionesRequest(
                fechaInicio = fechaInicio,
                fechaFin = fechaFin
            )
        )

        return response.isSuccessful
    }
}