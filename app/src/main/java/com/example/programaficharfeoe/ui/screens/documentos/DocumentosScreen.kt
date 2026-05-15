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
import com.example.programaficharfeoe.ui.components.ErrorView
import com.example.programaficharfeoe.ui.components.AppCard
import com.example.programaficharfeoe.ui.components.ShimmerDocumentItem
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import kotlinx.coroutines.delay

@Composable
fun DocumentosMenuScreen(
    navController: NavController
) {

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        delay(120)

        visible = true
    }

    val categorias = listOf(

        Triple(
            "Nóminas",
            "Recibos salariales",
            Icons.Default.PictureAsPdf
        ),

        Triple(
            "EPIs",
            "Equipos de protección",
            Icons.Default.Security
        ),

        Triple(
            "Formación",
            "Cursos y certificados",
            Icons.Default.School
        ),

        Triple(
            "Reconocimientos",
            "Informes médicos",
            Icons.Default.Description
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        AnimatedVisibility(

            visible = visible,

            enter = fadeIn(
                animationSpec = tween(700)
            ) + slideInVertically(

                initialOffsetY = { -80 },

                animationSpec = tween(700)
            )
        ) {

            Header()
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        AnimatedVisibility(

            visible = visible,

            enter = fadeIn(
                animationSpec = tween(650)
            ) + slideInVertically(

                initialOffsetY = { 40 },

                animationSpec = tween(650)
            )
        ) {

            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            categorias.forEachIndexed { index, categoria ->

                var itemVisible by remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(Unit) {

                    delay(index * 120L)

                    itemVisible = true
                }

                AnimatedVisibility(

                    visible = itemVisible,

                    enter = fadeIn(
                        animationSpec = tween(450)
                    ) + slideInVertically(

                        initialOffsetY = { 40 },

                        animationSpec = tween(450)
                    )
                ) {

                    CategoriaCard(

                        titulo = categoria.first,

                        subtitulo = categoria.second,

                        icono = categoria.third

                    ) {

                        when (categoria.first) {

                            "Nóminas" -> {
                                navController.navigate(
                                    "documentos/nomina/Nóminas"
                                ) {
                                    launchSingleTop = true
                                }
                            }

                            "EPIs" -> {
                                navController.navigate(
                                    "documentos/epis/EPIs"
                                ) {
                                    launchSingleTop = true
                                }
                            }

                            "Formación" -> {
                                navController.navigate(
                                    "documentos/formacion/Formación"
                                ) {
                                    launchSingleTop = true
                                }
                            }

                            "Reconocimientos" -> {
                                navController.navigate(
                                    "documentos/reconocimiento/Reconocimientos"
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
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

        HeaderListado(
            titulo = titulo,
            cantidad = state.documentos.size
        )

        Spacer(modifier = Modifier.height(18.dp))

        when {

            state.isLoading -> {

                LazyColumn(

                    verticalArrangement = Arrangement.spacedBy(12.dp)

                ) {

                    items(6) {

                        ShimmerDocumentItem()
                    }
                }
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
fun HeaderListado(
    titulo: String,
    cantidad: Int
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp)
    ) {

        Box(

            modifier = Modifier
                .fillMaxWidth()
                .background(

                    Brush.horizontalGradient(

                        listOf(
                            Color(0xFF1E3A8A),
                            Color(0xFF2563EB),
                            Color(0xFF3B82F6)
                        )
                    )
                )
                .padding(22.dp)
        ) {

            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(
                                Color.White.copy(alpha = 0.16f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            Icons.Default.Description,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(16.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = titulo,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "Tus documentos disponibles",
                            color = Color.White.copy(alpha = 0.88f)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Surface(

                    color = Color.White.copy(alpha = 0.16f),

                    shape = RoundedCornerShape(50)

                ) {

                    Row(

                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 8.dp
                        ),

                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    Color(0xFF22C55E),
                                    CircleShape
                                )
                        )

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Text(
                            text = "$cantidad documentos",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
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
        "epis" -> Icons.Default.Security
        "formacion" -> Icons.Default.School
        else -> Icons.Default.Description
    }

    val colorIcono = when (doc.tipo.lowercase()) {
        "nomina" -> Color(0xFFEF4444)
        "epis" -> Color(0xFFF59E0B)
        "formacion" -> Color(0xFF22C55E)
        else -> Color(0xFF3B82F6)
    }

    AppCard(
        modifier = Modifier
            .clickable { onClick() }
    ) {

        Column {

            // CABECERA VISUAL
            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        colorIcono.copy(alpha = 0.10f)
                    )
                    .padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                colorIcono.copy(alpha = 0.18f),
                                RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = colorIcono,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = doc.nombre,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Surface(

                            shape = RoundedCornerShape(50),

                            color = colorIcono.copy(alpha = 0.14f)

                        ) {

                            Text(

                                text = doc.tipo.uppercase(),

                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 5.dp
                                ),

                                color = colorIcono,

                                style = MaterialTheme.typography.bodySmall,

                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // FOOTER
            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically

            ) {

                Text(
                    text = "Pulsa para abrir",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "PDF",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = colorIcono
                )
            }
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