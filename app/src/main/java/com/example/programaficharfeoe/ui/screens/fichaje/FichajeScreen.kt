package com.example.programaficharfeoe.ui.screens.fichaje

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.utils.normalizarTimestamp
import com.example.programaficharfeoe.viewmodel.FichajeViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.programaficharfeoe.data.model.Fichaje

@Composable
fun FichajeScreen() {
    val context = LocalContext.current
    val viewModel: FichajeViewModel = viewModel()
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

    val fichajes = viewModel.fichajesLocales
    val cargando = viewModel.cargando
    val accionesDisponibles = viewModel.obtenerAccionesDisponibles()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        viewModel.cargarDatos(userId)
    }

    if (cargando) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Título
        Text(
            text = "Fichaje",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 ACCIONES DISPONIBLES
        Text(
            text = "Acciones disponibles",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (accionesDisponibles.isEmpty()) {
            Text("No hay acciones disponibles en este momento.")
        } else {
            accionesDisponibles.forEach { (accion, contexto) ->

                val colorBoton = when (accion) {
                    "ENTRADA" -> Color(0xFF4CAF50)
                    "SALIDA" -> Color(0xFFF44336)
                    "INICIO_VIAJE", "FIN_VIAJE" -> Color(0xFF2196F3)
                    "INICIO_DESCANSO", "FIN_DESCANSO" -> Color(0xFFFF9800)
                    else -> MaterialTheme.colorScheme.primary
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
                        .padding(vertical = 6.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorBoton
                    )
                ) {
                    Text(
                        text = "${accion.replace("_", " ")} - ${contexto.replace("_", " ")}"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 REGISTROS DEL DÍA
        Text(
            text = "Registros de hoy",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Permite que la lista sea deslizable
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (fichajes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay registros hoy",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        fichajes.sortedByDescending { it.fechaHora }
                    ) { fichaje ->
                        RegistroItem(fichaje)
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }

    // 🔹 MENSAJES
    viewModel.mensaje?.let { mensaje ->
        LaunchedEffect(mensaje) {
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            viewModel.limpiarMensaje()
        }
    }
}

@Composable
fun RegistroItem(fichaje: Fichaje) {
    val fechaFormateada = normalizarTimestamp(fichaje.fechaHora)

    // Limpiar y normalizar el tipo
    val partes = fichaje.tipo
        .trim()
        .replace("·", "")              // Elimina puntos intermedios
        .replace(" ", "_")             // Sustituye espacios por guiones bajos
        .replace("__", "_")            // Evita dobles guiones bajos
        .split("_")
        .filter { it.isNotBlank() }    // Elimina elementos vacíos

    // Última parte: contexto
    val contexto = partes.lastOrNull()?.uppercase() ?: ""

    // Resto: acción
    val accion = partes
        .dropLast(1)
        .joinToString(" ")
        .uppercase()

    val textoAccion = listOf(accion, contexto)
        .filter { it.isNotBlank() }
        .joinToString(" - ")

    // Color según el tipo de fichaje
    val colorIndicador = when {
        fichaje.tipo.startsWith("ENTRADA") -> Color(0xFF4CAF50)
        fichaje.tipo.startsWith("SALIDA") -> Color(0xFFF44336)
        fichaje.tipo.contains("DESCANSO") -> Color(0xFFFF9800)
        fichaje.tipo.contains("VIAJE") -> Color(0xFF2196F3)
        else -> MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Indicador de color
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(40.dp)
                .background(colorIndicador, RoundedCornerShape(2.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = textoAccion,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = fechaFormateada,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}