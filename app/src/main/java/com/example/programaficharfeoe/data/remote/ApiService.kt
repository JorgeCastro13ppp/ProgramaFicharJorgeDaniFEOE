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

    @GET("documentos")
    suspend fun getDocumentos(
        @Query("tipo") tipo: String
    ): Response<List<Documento>>

    @GET("vacaciones")
    suspend fun getVacaciones(): Response<List<Vacacion>>

    @POST("vacaciones")
    suspend fun solicitarVacaciones(
        @Body request: VacacionesRequest
    ): Response<Map<String, String>>

    @POST("fichar")
    suspend fun fichar(
        @Body request: FichajeRequest
    ): Response<Map<String, String>>

    @GET("fichajes")
    suspend fun getFichajes(): Response<List<Fichaje>>
}