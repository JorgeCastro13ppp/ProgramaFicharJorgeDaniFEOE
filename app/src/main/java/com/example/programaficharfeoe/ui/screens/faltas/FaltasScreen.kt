package com.example.programaficharfeoe.ui.screens.faltas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.ui.viewmodel.FaltasViewModel

@Composable
fun FaltasScreen(viewModel: FaltasViewModel = viewModel()) {

    LaunchedEffect(Unit) {
        viewModel.cargarFaltas()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(
            text = "Faltas",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {

            LazyColumn {
                items(viewModel.faltas) { falta ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(text = falta.fecha)
                            Text(text = falta.tipo)
                            Text(text = falta.descripcion)
                        }
                    }
                }
            }
        }
    }
}