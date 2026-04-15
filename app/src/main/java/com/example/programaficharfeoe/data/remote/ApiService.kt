package com.example.programaficharfeoe.data.remote

import com.example.programaficharfeoe.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("faltas")
    suspend fun getFaltas(): List<Falta>

    @GET("documentos")
    suspend fun getDocumentos(
        @Query("tipo") tipo: String
    ): List<Documento>

    @GET("vacaciones")
    suspend fun getVacaciones(): List<Vacacion>

    @POST("vacaciones")
    suspend fun solicitarVacaciones(
        @Body request: VacacionesRequest
    ): MessageResponse

    @POST("fichar")
    suspend fun ficharSimple(
        @Body request: FichajeRequest
    ): MessageResponse

    @GET("fichajes")
    suspend fun getFichajes(
        @Query("userId") userId: Int
    ): List<Fichaje>

    @POST("fichajes-eventos")
    suspend fun ficharEvento(
        @Body request: FichajeEventoRequest
    ): FichajeEventoResponse

    @GET("fichajes-eventos/hoy/{userId}")
    suspend fun getFichajesDelDia(
        @Path("userId") userId: Int
    ): List<Fichaje>

    @GET("fichajes-eventos/ultimo/{userId}")
    suspend fun getUltimoFichaje(
        @Path("userId") userId: Int
    ): Fichaje

    @POST("fichajes-eventos")
    suspend fun fichar(
        @Body request: FichajeEventoRequest
    ): FichajeEventoResponse

    @GET("fichajes-eventos/estado/{userId}")
    suspend fun getEstadoActual(
        @Path("userId") userId: Int
    ): EstadoActualResponse

    @GET("fichajes-eventos/siguiente-accion/{userId}")
    suspend fun getSiguientesAcciones(
        @Path("userId") userId: Int
    ): SiguientesAccionesResponse
}