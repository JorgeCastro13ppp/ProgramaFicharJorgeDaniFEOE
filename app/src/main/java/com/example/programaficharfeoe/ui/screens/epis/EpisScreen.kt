package com.example.programaficharfeoe.ui.screens.epis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Epi(
    val nombre: String,
    val estado: String,
    val fecha: String
)

@Composable
fun EpisScreen() {

    val epis = listOf(
        Epi("Casco de seguridad", "Entregado", "01/01/2025"),
        Epi("Chaleco reflectante", "Entregado", "15/02/2025"),
        Epi("Botas de seguridad", "Pendiente", "-")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "EPIs",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(epis) { epi ->
                EpiItem(epi)
            }
        }
    }
}

@Composable
fun EpiItem(epi: Epi) {

    var mensaje by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = epi.nombre,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Estado: ${epi.estado}",
                color = if (epi.estado == "Entregado")
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text("Fecha: ${epi.fecha}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                mensaje = "Disponible próximamente (backend)"
            }) {
                Text("Ver detalle")
            }

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(mensaje)
            }
        }
    }
}