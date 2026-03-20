package com.example.programaficharfeoe.data.repository

class FichajeRepository {

    suspend fun enviarQR(qr: String): String {
        // 🔥 Simulación de backend
        return "Fichaje registrado correctamente ✅ ($qr)"
    }
}