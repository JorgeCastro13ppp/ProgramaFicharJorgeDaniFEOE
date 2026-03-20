package com.example.programaficharfeoe.ui.screens.reconocimientos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Reconocimiento(
    val fecha: String,
    val estado: String
)

@Composable
fun ReconocimientosScreen() {

    val reconocimientos = listOf(
        Reconocimiento("15/01/2025", "Apto"),
        Reconocimiento("10/07/2024", "Apto"),
        Reconocimiento("01/03/2025", "Pendiente")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Reconocimientos Médicos",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(reconocimientos) { reconocimiento ->
                ReconocimientoItem(reconocimiento)
            }
        }
    }
}

@Composable
fun ReconocimientoItem(reconocimiento: Reconocimiento) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text("Fecha: ${reconocimiento.fecha}")

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Estado: ${reconocimiento.estado}",
                color = if (reconocimiento.estado == "Apto")
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                // 🔥 Futuro backend (ver detalle)
            }) {
                Text("Ver detalle")
            }
        }
    }
}