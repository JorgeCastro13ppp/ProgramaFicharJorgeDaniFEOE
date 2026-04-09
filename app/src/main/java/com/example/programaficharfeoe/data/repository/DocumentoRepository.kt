package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.data.remote.RetrofitClient

class DocumentoRepository {

    private val api = RetrofitClient.api

    suspend fun getDocumentos(tipo: String): List<Documento>? {
        val response = api.getDocumentos(tipo)
        return if (response.isSuccessful) response.body() else null
    }
}