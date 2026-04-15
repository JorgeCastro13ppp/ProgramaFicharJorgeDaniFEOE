package com.example.programaficharfeoe.data.model

import com.google.gson.annotations.SerializedName

data class Fichaje(

    @SerializedName("id")
    val id: Int,

    @SerializedName("userId")
    val user_id: Int,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("fechaHora")
    val fecha_hora: Long,

    @SerializedName("tipo")
    val tipo: String
)