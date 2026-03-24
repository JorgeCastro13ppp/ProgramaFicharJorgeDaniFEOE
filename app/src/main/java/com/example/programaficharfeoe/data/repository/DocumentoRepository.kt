package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.data.remote.RetrofitClient

class DocumentoRepository {

    private val api = RetrofitClient.instance

    suspend fun getEpis(): List<Documento>? {
        val response = api.getDocumentos("epi")
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getNominas(): List<Documento>? {
        val response = api.getDocumentos("nomina")
        return if (response.isSuccessful) response.body() else null
    }
}