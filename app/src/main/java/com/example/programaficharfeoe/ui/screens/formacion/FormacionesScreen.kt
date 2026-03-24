package com.example.programaficharfeoe.ui.screens.formacion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Curso(
    val nombre: String,
    val estado: String
)

@Composable
fun FormacionesScreen() {

    val cursos = listOf(
        Curso("Prevención de riesgos laborales", "Completado"),
        Curso("Uso de EPIs", "Completado"),
        Curso("Seguridad en el trabajo", "En curso")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Formación",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(cursos) { curso ->
                CursoItem(curso)
            }
        }
    }
}

@Composable
fun CursoItem(curso: Curso) {

    var mensaje by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = curso.nombre,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Estado: ${curso.estado}",
                color = if (curso.estado == "Completado")
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                mensaje = "Disponible próximamente (backend)"
            }) {
                Text("Ver diploma")
            }

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(mensaje)
            }
        }
    }
}