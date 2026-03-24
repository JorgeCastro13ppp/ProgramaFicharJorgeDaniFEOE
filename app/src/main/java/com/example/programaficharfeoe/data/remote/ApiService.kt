package com.example.programaficharfeoe.data.remote

import com.example.programaficharfeoe.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("faltas")
    suspend fun getFaltas(): Response<List<Falta>>

    @GET("documentos/{tipo}")
    suspend fun getDocumentos(
        @Path("tipo") tipo: String
    ): Response<List<Documento>>
}