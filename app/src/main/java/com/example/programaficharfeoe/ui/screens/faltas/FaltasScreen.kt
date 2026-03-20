package com.example.programaficharfeoe.ui.screens.faltas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Falta(
    val fecha: String,
    val motivo: String
)

@Composable
fun FaltasScreen() {

    val faltas = listOf(
        Falta("10/03/2025", "Enfermedad"),
        Falta("22/02/2025", "Asuntos personales")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Faltas de asistencia",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(faltas) { falta ->
                FaltaItem(falta)
            }
        }
    }
}

@Composable
fun FaltaItem(falta: Falta) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text("Fecha: ${falta.fecha}")
            Text("Motivo: ${falta.motivo}")
        }
    }
}