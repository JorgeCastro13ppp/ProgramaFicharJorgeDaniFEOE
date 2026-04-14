package com.example.programaficharfeoe.ui.screens.fichaje

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.example.programaficharfeoe.viewmodel.FichajeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FichajeScreen(
    contextoInicial: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: FichajeViewModel = viewModel()
    val userId = SessionManager.getUserId()

    val contexto = contextoInicial.uppercase()

    val fichajes = viewModel.fichajesLocales
    val cargando = viewModel.cargando

    // Solicitar permiso de ubicación
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        viewModel.cargarDatos(userId)
    }

    // Pantalla de carga
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

        // 🔹 Título
        Text(
            text = "Fichaje",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Contexto

        Text(
            text = "Contexto: $contexto",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Acciones
        Text("Acciones")
        Spacer(modifier = Modifier.height(8.dp))

        val acciones = listOf(
            "ENTRADA" to "SALIDA",
            "INICIO_VIAJE" to "FIN_VIAJE",
            "INICIO_DESCANSO" to "FIN_DESCANSO"
        )

        acciones.forEach { (izq, der) ->

            val puedeIzq =
                !cargando && viewModel.puedeFichar(izq, contexto)
            val puedeDer =
                !cargando && viewModel.puedeFichar(der, contexto)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            Toast.makeText(
                                context,
                                "Permiso de ubicación denegado",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        viewModel.fichar(
                            context,
                            userId,
                            contexto,
                            izq
                        )
                    },
                    enabled = puedeIzq,
                    modifier = Modifier
                        .weight(1f)
                        .height(70.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (puedeIzq)
                            Color(0xFF4CAF50)
                        else Color.LightGray
                    )
                ) {
                    Text(izq.replace("_", " "))
                }

                Button(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            Toast.makeText(
                                context,
                                "Permiso de ubicación denegado",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        viewModel.fichar(
                            context,
                            userId,
                            contexto,
                            der
                        )
                    },
                    enabled = puedeDer,
                    modifier = Modifier
                        .weight(1f)
                        .height(70.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (puedeDer)
                            Color(0xFFF44336)
                        else Color.LightGray
                    )
                ) {
                    Text(der.replace("_", " "))
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
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            if (fichajes.isEmpty()) {
                Text(
                    text = "No hay registros hoy",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(fichajes) { fichaje ->
                        RegistroItem(
                            tipo = fichaje.tipo,
                            timestamp = fichaje.fecha_hora
                        )
                    }
                }
            }
        }
    }

    // Mensajes
    viewModel.mensaje?.let { mensaje ->
        LaunchedEffect(mensaje) {
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            viewModel.mensaje = null
        }
    }
}

@Composable
fun RegistroItem(tipo: String, timestamp: Long) {
    val formatoHora = SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault())
    val fechaFormateada = formatoHora.format(Date(timestamp))

    // Separar acción y contexto (formato: ACCION · CONTEXTO)
    val partes = tipo.split("·")

    val accion = partes.getOrNull(0)
        ?.trim()
        ?.lowercase()
        ?.replace("_", " ")
        ?.replaceFirstChar { it.uppercase() }
        ?: "Desconocido"

    val contexto = partes.getOrNull(1)
        ?.trim()
        ?.lowercase()
        ?.replaceFirstChar { it.uppercase() }
        ?: ""

    val textoFinal = if (contexto.isNotEmpty()) {
        "$accion - $contexto"
    } else {
        accion
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = textoFinal,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = fechaFormateada,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )
    }
}