package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.data.remote.RetrofitClient

class NominasRepository {

    private val api = RetrofitClient.instance.create(ApiService::class.java)

    suspend fun getNominas(): List<Documento>? {
        val response = api.getDocumentos("nomina")

        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}