package com.example.programaficharfeoe.ui.screens.faltas

import androidx.compose.foundation.background
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
import com.example.programaficharfeoe.ui.components.LoadingView
import com.example.programaficharfeoe.ui.components.ErrorView
import com.example.programaficharfeoe.ui.components.AppCard
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.delay
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FaltasScreen(
    viewModel: FaltasViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.cargarFaltas()
    }

    val state = viewModel.uiState

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        delay(120)

        visible = true
    }

    val faltas = state.faltas

    val cargando = state.isLoading

    val justificadas = faltas.count { it.tipo.equals("JUSTIFICADA", true) }
    val retrasos = faltas.count { it.tipo.equals("RETRASO", true) }
    val injustificadas = faltas.count { it.tipo.equals("INJUSTIFICADA", true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // HEADER
        item {

            AnimatedVisibility(

                visible = visible,

                enter = fadeIn(
                    animationSpec = tween(700)
                ) + slideInVertically(

                    initialOffsetY = { -80 },

                    animationSpec = tween(700)
                )
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

                            Spacer(
                                modifier = Modifier.width(10.dp)
                            )

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
        }

        // RESUMEN PRO
            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    listOf(

                        Triple(
                            "Justificadas",
                            justificadas,
                            Color(0xFF22C55E)
                        ),

                        Triple(
                            "Retrasos",
                            retrasos,
                            Color(0xFFF59E0B)
                        ),

                        Triple(
                            "Injustificadas",
                            injustificadas,
                            Color(0xFFEF4444)
                        )

                    ).forEachIndexed { index, item ->

                        val visibleState = remember {
                            mutableStateOf(false)
                        }

                        LaunchedEffect(Unit) {

                            delay(index * 120L)

                            visibleState.value = true
                        }

                        Box(
                            modifier = Modifier.weight(1f)
                        ) {

                            androidx.compose.animation.AnimatedVisibility(

                                visible = visibleState.value,

                                enter = fadeIn(
                                    animationSpec = tween(500)
                                ) + slideInVertically(

                                    initialOffsetY = { 40 },

                                    animationSpec = tween(500)
                                )
                            ) {

                                Resumen(
                                    titulo = item.first,
                                    valor = item.second,
                                    color = item.third
                                )
                            }
                        }
                    }
                }
            }

        // HISTORIAL
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
                    LoadingView()
                }
            }

            state.error != null -> {

                item {

                    ErrorView(
                        message = state.error ?: "Error desconocido"
                    )
                }
            }

            faltas.isEmpty() -> {
                item {
                    EmptyFaltasState()
                }
            }

            else -> {

                itemsIndexed(
                    faltas.sortedByDescending { it.fecha }
                ) { index, falta ->

                    val visibleState = remember {
                        mutableStateOf(false)
                    }

                    LaunchedEffect(Unit) {

                        delay(index * 70L)

                        visibleState.value = true
                    }

                    AnimatedVisibility(

                        visible = visibleState.value,

                        enter = fadeIn(
                            animationSpec = tween(350)
                        ) + slideInVertically(

                            initialOffsetY = { 30 },

                            animationSpec = tween(350)
                        )
                    ) {

                        FaltaItem(falta)
                    }
                }
            }
        }
    }
}

@Composable
fun Resumen(
    titulo: String,
    valor: Int,
    color: Color,
    modifier: Modifier = Modifier
) {

    val infiniteTransition = rememberInfiniteTransition(
        label = ""
    )

    val scaleAnim by infiniteTransition.animateFloat(

        initialValue = 1f,

        targetValue = 1.35f,

        animationSpec = infiniteRepeatable(

            animation = tween(900),

            repeatMode = RepeatMode.Reverse
        ),

        label = ""
    )

    AppCard(
        modifier = modifier
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
                    .scale(scaleAnim)
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

    AppCard {

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
        Text("No tienes faltas registradas")
    }
}