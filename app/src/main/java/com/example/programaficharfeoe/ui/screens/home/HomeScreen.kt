package com.example.programaficharfeoe.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.screens.fichaje.RegistroItem
import com.example.programaficharfeoe.utils.getEstadoColor
import com.example.programaficharfeoe.viewmodel.DashboardViewModel
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    dashboardViewModel: DashboardViewModel
) {

    val userId = SessionManager.getUserId()

    val estado = dashboardViewModel.estadoActual
    val fichajes = dashboardViewModel.fichajesHoy
    val cargando = dashboardViewModel.cargando

    val diasRestantes = dashboardViewModel.diasVacacionesRestantes
    val diasLibres = dashboardViewModel.diasLibresRestantes
    val diasNavidad = dashboardViewModel.diasNavidadRestantes

    val estadoColor = getEstadoColor(estado)

    // PULL REFRESH
    val pullRefreshState = rememberPullRefreshState(
        refreshing = cargando,
        onRefresh = {
            userId?.let {
                dashboardViewModel.cargarDashboard(it)
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ESTADO
            item {
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
                                .size(14.dp)
                                .background(estadoColor, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text("Estado actual")

                            Text(
                                estado ?: "Desconocido",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // VACACIONES
            item {
                VacacionesCard(
                    restantes = diasRestantes,
                    libresRestantes = diasLibres,
                    navidadRestantes = diasNavidad
                )
            }

            // 🕓 ÚLTIMO FICHAJE
            item {
                Text("Último fichaje", fontWeight = FontWeight.Bold)
            }

            item {
                val ultimo = fichajes.lastOrNull()

                if (ultimo == null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay registros")
                        }
                    }
                } else {
                    RegistroItem(ultimo)
                }
            }

            // 📋 HISTORIAL
            item {
                Text("Actividad reciente", fontWeight = FontWeight.Bold)
            }

            items(fichajes.takeLast(3).reversed()) {
                RegistroItem(it)
            }
        }

        // INDICADOR ARRIBA
        PullRefreshIndicator(
            refreshing = cargando,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun VacacionesCard(
    restantes: Int?,
    libresRestantes: Int?,
    navidadRestantes: Int?
) {

    // Skeleton
    if (restantes == null) {
        SkeletonVacaciones()
        return
    }

    val diasAnim by animateIntAsState(restantes, label = "")
    val libresAnim by animateIntAsState(libresRestantes ?: 0, label = "")
    val navidadAnim by animateIntAsState(navidadRestantes ?: 0, label = "")

    val progresoObjetivo = restantes / 30f
    val progresoAnimado by animateFloatAsState(
        targetValue = progresoObjetivo,
        label = ""
    )

    val colorBarra = when {
        restantes < 5 -> Color.Red
        restantes < 10 -> Color(0xFFF59E0B)
        else -> Color(0xFF22C55E)
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

        Column(modifier = Modifier.padding(16.dp)) {

            Text("Vacaciones", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "$diasAnim",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text("días disponibles")

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = progresoAnimado,
                color = colorBarra,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                VacacionesMiniItem("Libres", libresRestantes?.let { libresAnim })
                VacacionesMiniItem("Navidad", navidadRestantes?.let { navidadAnim })
            }
        }
    }
}

@Composable
fun SkeletonVacaciones() {

    val shimmer = Brush.linearGradient(
        listOf(
            Color.Gray.copy(alpha = 0.3f),
            Color.Gray.copy(alpha = 0.1f),
            Color.Gray.copy(alpha = 0.3f)
        )
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(20.dp)
                    .background(shimmer)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .height(32.dp)
                    .background(shimmer)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(shimmer)
            )
        }
    }
}

@Composable
fun VacacionesMiniItem(
    titulo: String,
    valor: Int?
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            valor?.toString() ?: "--",
            fontWeight = FontWeight.Bold
        )

        Text(
            titulo,
            style = MaterialTheme.typography.bodySmall
        )
    }
}