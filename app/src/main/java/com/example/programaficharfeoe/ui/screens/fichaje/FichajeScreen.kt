package com.example.programaficharfeoe.ui.screens.fichaje

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.data.model.Fichaje
import com.example.programaficharfeoe.ui.components.FichajeHeader
import com.example.programaficharfeoe.utils.normalizarTimestamp
import com.example.programaficharfeoe.viewmodel.DashboardViewModel
import com.example.programaficharfeoe.viewmodel.FichajeViewModel
import com.example.programaficharfeoe.ui.utils.obtenerColorFichaje
import com.example.programaficharfeoe.ui.utils.obtenerIconoFichaje
import com.example.programaficharfeoe.ui.components.LoadingView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.itemsIndexed
import com.example.programaficharfeoe.ui.components.AppCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FichajeScreen() {

    val context = LocalContext.current
    val fichajeViewModel: FichajeViewModel = viewModel()
    val dashboardViewModel: DashboardViewModel = viewModel()

    val userId = SessionManager.getUserId()

    if (userId == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Usuario no autenticado")
        }
        return
    }

    val state = fichajeViewModel.uiState

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    // CARGA GLOBAL
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        dashboardViewModel.cargarDashboard(userId)
        fichajeViewModel.cargarDatos(userId)
    }

    if (state.cargando) {

        LoadingView()

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // HEADER
        FichajeHeader(
            estadoActual = dashboardViewModel.uiState.estadoActual
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (state.accionesDisponibles.isEmpty()) {
            Text("No hay acciones disponibles ahora mismo")
        } else {
            state.accionesDisponibles.forEachIndexed { index, accionCompleta ->

                val visibleState = remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(Unit) {

                    delay(index * 90L)

                    visibleState.value = true
                }

                val texto = accionCompleta.replace("_", " ")

                val color = obtenerColorFichaje(accionCompleta)

                AnimatedVisibility(

                    visible = visibleState.value,

                    enter = fadeIn(
                        animationSpec = tween(400)
                    ) + slideInVertically(

                        initialOffsetY = { 40 },

                        animationSpec = tween(400)
                    )
                ) {

                    AppCard(
                        modifier = Modifier
                            .padding(vertical = 6.dp),

                        onClick = {

                            fichajeViewModel.fichar(
                                context,
                                userId,
                                accionCompleta
                            )

                            kotlinx.coroutines.CoroutineScope(
                                kotlinx.coroutines.Dispatchers.Main
                            ).launch {

                                delay(200)

                                dashboardViewModel.cargarDashboard(userId)
                            }
                        }
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(
                                    color,
                                    RoundedCornerShape(18.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                texto,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // REGISTROS DEL DÍA
        Text(
            text = "Registros de hoy",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {

            if (state.fichajesLocales.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay registros hoy")
                }

            } else {

                LazyColumn(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    itemsIndexed(
                        state.fichajesLocales
                            .sortedByDescending { it.fechaHora }
                    ) { index, fichaje ->
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

                            Column {

                                RegistroItem(fichaje)

                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
    // MENSAJES
    state.mensaje?.let { mensaje ->

        LaunchedEffect(mensaje) {

            Toast.makeText(
                context,
                mensaje,
                Toast.LENGTH_SHORT
            ).show()

            fichajeViewModel.limpiarMensaje()
        }
    }
}

@Composable
fun RegistroItem(
    fichaje: Fichaje
) {

    val tipo = fichaje.tipo.uppercase()

    val color = obtenerColorFichaje(tipo)

    val icono = obtenerIconoFichaje(tipo)

    AppCard()
    {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color.copy(alpha = 0.15f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icono,
                    null,
                    tint = color
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = tipo.replace("_", " "),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = normalizarTimestamp(
                        fichaje.fechaHora
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}