package com.example.programaficharfeoe.data.model

data class VacacionesResumenResponse(
    val anio: Int,

    val diasNavidadUsados: Int,
    val diasNavidadRestantes: Int,

    val diasLibresUsados: Int,
    val diasLibresRestantes: Int,

    val diasTotalesRestantes: Int
)