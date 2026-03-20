package com.example.programaficharfeoe.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onGoToFichaje: () -> Unit,
    onGoToVacaciones: () -> Unit,
    onGoToNominas: () -> Unit,
    onGoToFaltas: () -> Unit,
    onGoToReconocimientos: () -> Unit,
    onGoToFormacion: () -> Unit,
    onGoToEpis: () -> Unit,
    onLogout: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Bienvenido 👋",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Panel de empleado",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        DashboardCard("Fichaje QR", "Registrar entrada/salida", onGoToFichaje)
        Spacer(modifier = Modifier.height(12.dp))

        DashboardCard("Vacaciones", "Solicitar días libres", onGoToVacaciones)
        Spacer(modifier = Modifier.height(12.dp))

        DashboardCard("Nóminas", "Consultar recibos", onGoToNominas)
        Spacer(modifier = Modifier.height(12.dp))

        DashboardCard("Faltas", "Consultar ausencias", onGoToFaltas)
        Spacer(modifier = Modifier.height(12.dp))

        DashboardCard("Reconocimientos", "Control médico laboral", onGoToReconocimientos)
        Spacer(modifier = Modifier.height(12.dp))

        DashboardCard("Formación", "Cursos y diplomas", onGoToFormacion)
        Spacer(modifier = Modifier.height(12.dp))

        DashboardCard("EPIs", "Equipos de protección", onGoToEpis)

        Spacer(modifier = Modifier.height(24.dp))

        LogoutCard(onLogout)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DashboardCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun LogoutCard(onLogout: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLogout() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Cerrar sesión",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Salir de la aplicación",
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}