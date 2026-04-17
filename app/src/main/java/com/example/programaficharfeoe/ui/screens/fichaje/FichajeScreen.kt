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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.data.model.Fichaje
import com.example.programaficharfeoe.utils.normalizarTimestamp
import com.example.programaficharfeoe.viewmodel.FichajeViewModel

@Composable
fun FichajeScreen() {

    val context = LocalContext.current
    val viewModel: FichajeViewModel = viewModel()
    val userId = SessionManager.getUserId()

    if (userId == null) return

    val fichajes = viewModel.fichajesLocales
    val cargando = viewModel.cargando
    val acciones = viewModel.obtenerAccionesDisponibles()

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        viewModel.cargarDatos(userId)
    }

    if (cargando) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item {

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
                    Column {

                        Text(
                            "Fichaje",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            "Acciones disponibles en este momento",
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // BOTONES ACCIONES
        items(acciones) { (accion, contexto) ->

            val colorBoton = when (accion) {
                "ENTRADA" -> Color(0xFF22C55E)
                "SALIDA" -> Color(0xFFEF4444)
                "INICIO_VIAJE", "FIN_VIAJE" -> Color(0xFF3B82F6)
                "INICIO_DESCANSO", "FIN_DESCANSO" -> Color(0xFFF59E0B)
                else -> MaterialTheme.colorScheme.primary
            }

            val icono = when (accion) {
                "ENTRADA" -> Icons.Default.Login
                "SALIDA" -> Icons.Default.Logout
                "INICIO_VIAJE", "FIN_VIAJE" -> Icons.Default.DirectionsCar
                else -> Icons.Default.Coffee
            }

            Button(
                onClick = {
                    viewModel.fichar(
                        context,
                        userId,
                        contexto,
                        accion
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorBoton
                )
            ) {

                Icon(icono, null)

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "${accion.replace("_", " ")} - $contexto",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Registros de hoy",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (fichajes.isEmpty()) {

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay registros hoy")
                    }
                }
            }

        } else {

            items(
                fichajes.sortedByDescending { it.fechaHora }
            ) { fichaje ->

                RegistroPremiumItem(fichaje)
            }
        }
    }

    viewModel.mensaje?.let {
        LaunchedEffect(it) {
            Toast.makeText(
                context,
                it,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.limpiarMensaje()
        }
    }
}

@Composable
fun RegistroPremiumItem(
    fichaje: Fichaje
) {

    val tipo = fichaje.tipo.uppercase()

    val color = when {
        tipo.startsWith("ENTRADA") -> Color(0xFF22C55E)
        tipo.startsWith("SALIDA") -> Color(0xFFEF4444)
        tipo.contains("DESCANSO") -> Color(0xFFF59E0B)
        tipo.contains("VIAJE") -> Color(0xFF3B82F6)
        else -> MaterialTheme.colorScheme.primary
    }

    val icono = when {
        tipo.startsWith("ENTRADA") -> Icons.Default.Login
        tipo.startsWith("SALIDA") -> Icons.Default.Logout
        tipo.contains("VIAJE") -> Icons.Default.DirectionsCar
        else -> Icons.Default.AccessTime
    }

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