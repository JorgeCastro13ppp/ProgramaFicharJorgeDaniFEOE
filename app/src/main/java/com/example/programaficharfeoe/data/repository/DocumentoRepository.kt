package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.utils.ApiResult
import com.example.programaficharfeoe.utils.safeApiCall

class DocumentoRepository(private val api: ApiService) {

    suspend fun getDocumentos(
        tipo: String
    ): ApiResult<List<Documento>> {

        return safeApiCall {

            api.getDocumentos(tipo)
        }
    }
}