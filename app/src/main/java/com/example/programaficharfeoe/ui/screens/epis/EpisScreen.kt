package com.example.programaficharfeoe.ui.screens.epis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.viewmodel.EpisViewModel

@Composable
fun EpisScreen(viewModel: EpisViewModel = viewModel()) {

    // 🔥 Cargar datos al entrar
    LaunchedEffect(Unit) {
        viewModel.cargarEpis()
    }

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

        when {
            viewModel.isLoading -> {
                CircularProgressIndicator()
            }

            viewModel.error != null -> {
                Text(
                    text = viewModel.error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                LazyColumn {
                    items(viewModel.epis) { epi ->
                        EpiItem(epi)
                    }
                }
            }
        }
    }
}

@Composable
fun EpiItem(epi: Documento) {

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

            Text("Estado: ${epi.nombre}")

            Spacer(modifier = Modifier.height(4.dp))

            Text("Fecha: ${epi.tipo}")
        }
    }
}