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
        FichajeHeader()

        Spacer(modifier = Modifier.height(12.dp))

        if (state.accionesDisponibles.isEmpty()) {
            Text("No hay acciones disponibles ahora mismo")
        } else {

            state.accionesDisponibles.forEach { accionCompleta ->

                val texto = accionCompleta.replace("_", " ")

                val color = obtenerColorFichaje(accionCompleta)

                Button(
                    onClick = {
                        fichajeViewModel.fichar(
                            context,
                            userId,
                            accionCompleta
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = color
                    )
                ) {
                    Text(texto)
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

                    items(state.fichajesLocales.sortedByDescending { it.fechaHora }) { fichaje ->
                        RegistroItem(fichaje)
                        HorizontalDivider()
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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