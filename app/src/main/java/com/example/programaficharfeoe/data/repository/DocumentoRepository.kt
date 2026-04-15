package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.data.remote.ApiService

class DocumentoRepository(private val api: ApiService) {

    suspend fun getDocumentos(tipo: String): Result<List<Documento>> =
        runCatching {
            api.getDocumentos(tipo)
        }
}