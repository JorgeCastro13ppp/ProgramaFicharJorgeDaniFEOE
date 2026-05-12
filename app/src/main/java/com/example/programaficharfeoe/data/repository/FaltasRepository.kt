package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.Falta
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.utils.ApiResult
import com.example.programaficharfeoe.utils.safeApiCall

class FaltasRepository(private val api: ApiService) {

    suspend fun getFaltas():
            ApiResult<List<Falta>> {

        return safeApiCall {

            api.getFaltas()
        }
    }
}