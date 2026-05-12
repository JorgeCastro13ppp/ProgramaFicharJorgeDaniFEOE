package com.example.programaficharfeoe.ui.screens.documentos

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.viewmodel.DocumentosViewModel
import com.example.programaficharfeoe.ui.components.LoadingView
import com.example.programaficharfeoe.ui.components.ErrorView
import com.example.programaficharfeoe.ui.components.AppCard

@Composable
fun DocumentosMenuScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Header()

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Categorías",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

            CategoriaCard(
                titulo = "Nóminas",
                subtitulo = "Recibos salariales",
                icono = Icons.Default.PictureAsPdf
            ) {
                navController.navigate("documentos/nomina/Nóminas") {
                    launchSingleTop = true
                }
            }

            CategoriaCard(
                titulo = "EPIs",
                subtitulo = "Equipos de protección",
                icono = Icons.Default.Security
            ) {
                navController.navigate("documentos/epis/EPIs") {
                    launchSingleTop = true
                }
            }

            CategoriaCard(
                titulo = "Formación",
                subtitulo = "Cursos y certificados",
                icono = Icons.Default.School
            ) {
                navController.navigate("documentos/formacion/Formación") {
                    launchSingleTop = true
                }
            }

            CategoriaCard(
                titulo = "Reconocimientos",
                subtitulo = "Informes médicos",
                icono = Icons.Default.Description
            ) {
                navController.navigate("documentos/reconocimiento/Reconocimientos") {
                    launchSingleTop = true
                }
            }
        }
    }
}

@Composable
fun Header() {

    // HEADER
    Card(
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF1E3A8A),
                            Color(0xFF2563EB)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.Description,
                    null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {

                    Text(
                        "Documentos",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Accede y descarga tus archivos",
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoriaCard(
    titulo: String,
    subtitulo: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    AppCard(
        modifier = Modifier
            .height(92.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        Color(0xFF2D6CFF).copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = titulo,
                    tint = Color(0xFF2D6CFF)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = subtitulo,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


// Listado documentos

@Composable
fun DocumentosScreen(
    titulo: String,
    tipo: String
) {

    val context = LocalContext.current
    val viewModel: DocumentosViewModel = viewModel()

    val state = viewModel.uiState

    LaunchedEffect(tipo) {
        viewModel.cargarDocumentos(tipo)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        HeaderListado(titulo)

        Spacer(modifier = Modifier.height(18.dp))

        when {

            state.isLoading -> {
                LoadingView()
            }

            state.error != null -> {
                ErrorView(
                    message = state.error ?: "Error desconocido"
                )
            }

            state.documentos.isEmpty() -> {
                EmptyState()
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        state.documentos.sortedByDescending { it.id }
                    ) { doc ->

                        DocumentoItem(
                            doc = doc,
                            onClick = {
                                try {

                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(doc.downloadUrl)
                                    )

                                    context.startActivity(
                                        Intent.createChooser(
                                            intent,
                                            "Abrir documento con"
                                        )
                                    )

                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "No se pudo abrir el documento",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderListado(titulo: String) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF1E3A8A),
                            Color(0xFF2563EB)
                        )
                    )
                )
                .padding(20.dp)
        ) {

            Column {

                Text(
                    text = titulo,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Tus documentos disponibles",
                    color = Color.White.copy(alpha = 0.90f)
                )
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
        "epis" -> Icons.Default.Security
        "formacion" -> Icons.Default.School
        else -> Icons.Default.Description
    }

    AppCard(
        modifier = Modifier
            .clickable { onClick() }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color(0xFF2D6CFF).copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = Color(0xFF2D6CFF)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doc.nombre,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Pulsa para abrir",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay documentos disponibles",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}