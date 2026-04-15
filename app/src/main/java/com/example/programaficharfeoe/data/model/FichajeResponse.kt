package com.example.programaficharfeoe.data.model

import com.google.gson.annotations.SerializedName

data class FichajeResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("fechaHora")
    val fechaHora: Long,

    @SerializedName("tipo")
    val tipo: String
)