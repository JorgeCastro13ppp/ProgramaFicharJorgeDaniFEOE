package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Fichaje
import com.example.programaficharfeoe.data.model.FichajeRequest
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.data.remote.RetrofitClient

class FichajeRepository {

    private val api = RetrofitClient.instance.create(ApiService::class.java)

    suspend fun fichar(token: String, tipo: String): Boolean {
        val response = api.fichar(
            FichajeRequest(
                token = token,
                tipo = tipo
            )
        )
        return response.isSuccessful
    }

    suspend fun obtenerFichajes(): List<Fichaje>? {
        val response = api.getFichajes()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun obtenerUltimoFichaje(): String? {
        val lista = obtenerFichajes()
        return lista?.lastOrNull()?.tipo
    }
}