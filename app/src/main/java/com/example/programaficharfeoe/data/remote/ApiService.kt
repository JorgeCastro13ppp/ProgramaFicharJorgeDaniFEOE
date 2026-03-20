package com.example.programaficharfeoe.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class FichajeRequest(
    val qr: String
)

data class FichajeResponse(
    val message: String
)

interface ApiService {

    @POST("fichaje")
    suspend fun enviarFichaje(
        @Body request: FichajeRequest
    ): Response<FichajeResponse>
}