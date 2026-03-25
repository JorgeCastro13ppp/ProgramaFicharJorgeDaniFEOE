package com.example.programaficharfeoe.ui.screens.fichaje

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.viewmodel.FichajeViewModel

@Composable
fun FichajeScreen(
    onScanQR: (String) -> Unit
) {

    val viewModel: FichajeViewModel = viewModel()

    val tipoDisponible = viewModel.tipoDisponible
    val mensaje = viewModel.mensaje

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Fichaje", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(30.dp))

        when (tipoDisponible) {

            "entrada" -> {
                Button(
                    onClick = { onScanQR("entrada") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fichar Entrada")
                }
            }

            "salida" -> {
                Button(
                    onClick = { onScanQR("salida") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fichar Salida")
                }
            }

            else -> {
                Text("Cargando...")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        mensaje?.let {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = it,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}