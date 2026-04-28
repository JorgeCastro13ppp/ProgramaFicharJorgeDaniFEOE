package com.example.programaficharfeoe.ui.screens.faltas

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.WorkOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.data.model.Falta
import com.example.programaficharfeoe.viewmodel.FaltasViewModel

@Composable
fun FaltasScreen(
    viewModel: FaltasViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.cargarFaltas()
    }

    val faltas = viewModel.faltas
    val cargando = viewModel.isLoading

    val justificadas = faltas.count { it.tipo.equals("JUSTIFICADA", true) }
    val retrasos = faltas.count { it.tipo.equals("RETRASO", true) }
    val injustificadas = faltas.count { it.tipo.equals("INJUSTIFICADA", true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 🔵 HEADER
        item {
            Card(shape = RoundedCornerShape(24.dp)) {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(Icons.Default.EventBusy, null, tint = Color.White)

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                "Faltas",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "Incidencias y ausencias",
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }

        // 📊 RESUMEN PRO
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                ResumenPro("Justificadas", justificadas, Color(0xFF22C55E), Modifier.weight(1f))
                ResumenPro("Retrasos", retrasos, Color(0xFFF59E0B), Modifier.weight(1f))
                ResumenPro("Injustificadas", injustificadas, Color(0xFFEF4444), Modifier.weight(1f))
            }
        }

        // 📋 HISTORIAL
        item {
            Text(
                "Historial",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        when {

            cargando -> {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            faltas.isEmpty() -> {
                item {
                    EmptyFaltasState()
                }
            }

            else -> {

                items(faltas.sortedByDescending { it.fecha }) { falta ->
                    FaltaItem(falta)
                }
            }
        }
    }
}

@Composable
fun ResumenPro(
    titulo: String,
    valor: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDark) 0.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(valor.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                titulo,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun FaltaItem(falta: Falta) {

    val tipo = falta.tipo.uppercase()

    val (color, icono) = when (tipo) {
        "JUSTIFICADA" -> Color(0xFF22C55E) to Icons.Default.WorkOff
        "RETRASO" -> Color(0xFFF59E0B) to Icons.Default.WarningAmber
        "INJUSTIFICADA" -> Color(0xFFEF4444) to Icons.Default.EventBusy
        else -> Color(0xFFEF4444) to Icons.Default.EventBusy
    }

    val isDark = isSystemInDarkTheme()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDark) 0.dp else 3.dp
        )
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, null, tint = color)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    falta.fecha,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(falta.descripcion)

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = when (tipo) {
                        "JUSTIFICADA" -> "Justificada"
                        "RETRASO" -> "Retraso"
                        "INJUSTIFICADA" -> "Injustificada"
                        else -> tipo
                    },
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EmptyFaltasState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("No tienes faltas registradas 🎉")
    }
}