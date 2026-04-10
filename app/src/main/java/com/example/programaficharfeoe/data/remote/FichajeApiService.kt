package com.example.programaficharfeoe.data.remote

import com.example.programaficharfeoe.data.model.EstadoActualResponse
import com.example.programaficharfeoe.data.model.FichajeEventoRequest
import com.example.programaficharfeoe.data.model.FichajeEventoResponse
import com.example.programaficharfeoe.data.model.FichajeResponse
import com.example.programaficharfeoe.data.model.SiguientesAccionesResponse
import retrofit2.http.*

interface FichajeApiService {

    @POST("fichajes-eventos")
    suspend fun fichar(
        @Body request: FichajeEventoRequest
    ): FichajeEventoResponse

    @GET("fichajes-eventos/hoy/{userId}")
    suspend fun getFichajesDelDia(
        @Path("userId") userId: Int
    ): List<FichajeResponse>

    @GET("fichajes-eventos/ultimo/{userId}")
    suspend fun getUltimoFichaje(
        @Path("userId") userId: Int
    ): FichajeResponse

    @GET("fichajes-eventos/estado/{userId}")
    suspend fun getEstadoActual(
        @Path("userId") userId: Int
    ): EstadoActualResponse

    @GET("fichajes-eventos/siguiente-accion/{userId}")
    suspend fun getSiguientesAcciones(
        @Path("userId") userId: Int
    ): SiguientesAccionesResponse
}