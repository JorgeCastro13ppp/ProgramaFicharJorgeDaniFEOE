package com.example.programaficharfeoe.ui.screens.nominas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.viewmodel.NominasViewModel

@Composable
fun NominasScreen(viewModel: NominasViewModel = viewModel()) {

    // 🔥 Cargar datos al entrar
    LaunchedEffect(Unit) {
        viewModel.cargarNominas()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Nóminas",
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
                    items(viewModel.nominas) { nomina ->
                        NominaItem(nomina)
                    }
                }
            }
        }
    }
}

@Composable
fun NominaItem(nomina: Documento) {

    var mensaje by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = nomina.nombre,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text("Estado: ${nomina.nombre}")

            Spacer(modifier = Modifier.height(4.dp))

            Text("Fecha: ${nomina.tipo}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                mensaje = "PDF disponible cuando backend esté listo"
            }) {
                Text("Ver PDF")
            }

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(mensaje)
            }
        }
    }
}