package com.example.programaficharfeoe.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text("Panel de empleado")
            }

            TextButton(onClick = onLogout) {
                Text("Cerrar sesión")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        MenuCard("Fichaje QR", "Registrar entrada/salida") {
            onNavigate("fichaje")
        }

        MenuCard("Vacaciones", "Solicitar días libres") {
            onNavigate("vacaciones")
        }

        MenuCard("Nóminas", "Consultar recibos") {
            onNavigate("nominas")
        }

        MenuCard("Faltas", "Consultar ausencias") {
            onNavigate("faltas")
        }

        MenuCard("Reconocimientos", "Control médico laboral") {
            onNavigate("reconocimientos")
        }

        MenuCard("Formación", "Cursos y diplomas") {
            onNavigate("formacion")
        }

        MenuCard("EPIs", "Equipos de protección") {
            onNavigate("epis")
        }
    }
}

@Composable
fun MenuCard(
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}