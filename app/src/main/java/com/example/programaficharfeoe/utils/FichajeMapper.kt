package com.example.programaficharfeoe.utils

import com.example.programaficharfeoe.data.model.Fichaje
import com.example.programaficharfeoe.data.model.FichajeResponse

fun FichajeResponse.toFichaje(): Fichaje {
    return Fichaje(
        id = id,
        userId = userId,
        username = null,
        fechaHora = normalizarTimestamp(fechaHora),
        tipo = tipo.uppercase()
    )
}