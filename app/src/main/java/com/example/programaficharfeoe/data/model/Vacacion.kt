package com.example.programaficharfeoe.data.model

import com.google.gson.annotations.SerializedName

data class Vacacion(
    val id: Int,
    val userId: Int,
    val fechaInicio: String,
    val fechaFin: String,
    val estado: String
)