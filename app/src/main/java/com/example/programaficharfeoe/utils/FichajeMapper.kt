package com.example.programaficharfeoe.utils

import com.example.programaficharfeoe.data.model.Fichaje
import com.example.programaficharfeoe.data.model.FichajeResponse

fun FichajeResponse.toFichaje(): Fichaje {
    return Fichaje(
        id = this.id,
        user_id = this.userId,
        tipo = this.tipo.uppercase(),
        fecha_hora = normalizarTimestamp(this.fechaHora)
    )
}