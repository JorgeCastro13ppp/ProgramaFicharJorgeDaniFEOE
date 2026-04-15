package com.example.programaficharfeoe.data.model
data class Documento(
    val id: Int,
    val userId: Int,
    val nombre: String,
    val tipo: String,
    val downloadUrl: String
)