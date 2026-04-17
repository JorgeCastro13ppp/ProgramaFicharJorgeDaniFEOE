package com.example.programaficharfeoe.ui.screens.faltas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

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
                        Icons.Default.EventBusy,
                        null,
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {

                        Text(
                            "Faltas",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Consulta incidencias y ausencias",
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // RESUMEN
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            ResumenCard(
                titulo = "Total",
                valor = faltas.size.toString(),
                modifier = Modifier.weight(1f)
            )

            ResumenCard(
                titulo = "Justificadas",
                valor = faltas.count {
                    it.tipo.contains(
                        "just",
                        true
                    )
                }.toString(),
                modifier = Modifier.weight(1f)
            )

            ResumenCard(
                titulo = "Pendientes",
                valor = faltas.count {
                    it.tipo.contains(
                        "pend",
                        true
                    )
                }.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Historial",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        when {

            cargando -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            faltas.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay faltas registradas"
                    )
                }
            }

            else -> {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(faltas) { falta ->

                        val colorEstado =
                            when {
                                falta.tipo.contains(
                                    "just",
                                    true
                                ) -> Color(0xFF22C55E)

                                falta.tipo.contains(
                                    "pend",
                                    true
                                ) -> Color(0xFFF59E0B)

                                else -> Color(0xFFEF4444)
                            }

                        val icono =
                            when {
                                falta.tipo.contains(
                                    "just",
                                    true
                                ) -> Icons.Default.WorkOff

                                falta.tipo.contains(
                                    "pend",
                                    true
                                ) -> Icons.Default.WarningAmber

                                else -> Icons.Default.EventBusy
                            }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor =
                                    MaterialTheme.colorScheme.surfaceVariant.copy(
                                        alpha = 0.45f
                                    )
                            )
                        ) {

                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .background(
                                            colorEstado.copy(alpha = 0.14f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        icono,
                                        null,
                                        tint = colorEstado
                                    )
                                }

                                Spacer(
                                    modifier = Modifier.width(14.dp)
                                )

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = falta.fecha,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(
                                        modifier = Modifier.height(4.dp)
                                    )

                                    Text(
                                        text = falta.descripcion
                                    )

                                    Spacer(
                                        modifier = Modifier.height(6.dp)
                                    )

                                    Text(
                                        text = falta.tipo.uppercase(),
                                        color = colorEstado,
                                        fontWeight = FontWeight.Bold
                                    )
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
fun ResumenCard(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.45f
                )
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = valor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}