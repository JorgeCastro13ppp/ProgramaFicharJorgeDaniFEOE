package com.example.programaficharfeoe.di

import com.example.programaficharfeoe.data.remote.RetrofitInstance
import com.example.programaficharfeoe.data.repository.*

object AppModule {

    private val api = RetrofitInstance.api

    val authRepository by lazy { AuthRepository(api) }
    val documentoRepository by lazy { DocumentoRepository(api) }
    val faltasRepository by lazy { FaltasRepository(api) }
    val fichajeRepository by lazy { FichajeRepository(api) }
    val vacacionesRepository by lazy { VacacionesRepository(api) }
}