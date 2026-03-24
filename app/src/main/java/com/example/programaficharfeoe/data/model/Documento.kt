package com.example.programaficharfeoe.data.model

data class Documento(
    val id: Int,
    val nombre: String,
    val tipo: String,
    val url: String? = null
)