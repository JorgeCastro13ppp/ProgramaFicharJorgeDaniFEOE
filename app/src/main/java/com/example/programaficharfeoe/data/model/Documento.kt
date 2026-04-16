package com.example.programaficharfeoe.data.model

import com.google.gson.annotations.SerializedName

data class Documento(
    val id: Int,
    val userId: Int,
    val username: String?,
    val nombre: String,
    val tipo: String,

    @SerializedName("url")
    val downloadUrl: String
)