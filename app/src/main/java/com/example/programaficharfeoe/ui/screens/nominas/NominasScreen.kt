package com.example.programaficharfeoe.ui.screens.nominas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Nomina(
    val mes: String,
    val anio: Int
)

@Composable
fun NominasScreen() {

    // 🔥 Datos simulados (luego vendrán del backend)
    val nominas = listOf(
        Nomina("Enero", 2025),
        Nomina("Febrero", 2025),
        Nomina("Marzo", 2025),
        Nomina("Abril", 2025)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Mis Nóminas",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(nominas) { nomina ->
                NominaItem(nomina)
            }
        }
    }
}

@Composable
fun NominaItem(nomina: Nomina) {

    var mensaje by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text("${nomina.mes} ${nomina.anio}")

                Button(onClick = {
                    mensaje = "Disponible próximamente (backend)"
                }) {
                    Text("Ver")
                }
            }

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(mensaje)
            }
        }
    }
}