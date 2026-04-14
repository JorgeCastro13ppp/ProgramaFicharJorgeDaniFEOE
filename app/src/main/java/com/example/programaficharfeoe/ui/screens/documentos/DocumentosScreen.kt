package com.example.programaficharfeoe.ui.screens.documentos

import DocumentosViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.utils.Constants

@Composable
fun DocumentosScreen(
    titulo: String,
    tipo: String
) {
    val context = LocalContext.current
    val viewModel: DocumentosViewModel = viewModel()

    val docs by viewModel.documentos
    val isLoading by viewModel.isLoading
    val error by viewModel.error

    // URL base del backend
    val baseUrl = Constants.BASE_URL

    LaunchedEffect(tipo) {
        viewModel.cargarDocumentos(tipo)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                LazyColumn {
                    items(docs.sortedByDescending { it.id }) { doc ->
                        DocumentoItem(
                            doc = doc,
                            onClick = {
                                // Construir la URL correctamente
                                val urlCompleta = when {
                                    doc.url.startsWith("http://") ||
                                            doc.url.startsWith("https://") -> {
                                        // La URL ya es completa
                                        doc.url.replace("localhost", "192.168.1.15")
                                    }
                                    else -> {
                                        // La URL es relativa
                                        "$baseUrl/${doc.url.trimStart('/')}"
                                    }
                                }

                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(urlCompleta)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }

                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DocumentoItem(
    doc: Documento,
    onClick: () -> Unit
) {
    val icono = when (doc.tipo.lowercase()) {
        "nomina" -> Icons.Default.PictureAsPdf
        "formacion" -> Icons.Default.School
        "epis" -> Icons.Default.Security
        else -> Icons.Default.Description
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = doc.nombre,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}