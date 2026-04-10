package com.example.programaficharfeoe.ui.screens.fichaje

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.utils.calcularTiempos
import com.example.programaficharfeoe.utils.formatearTiempo
import com.example.programaficharfeoe.utils.toFichaje
import com.example.programaficharfeoe.viewmodel.FichajeViewModel
import kotlinx.coroutines.delay

@Composable
fun FichajeScreen(
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: FichajeViewModel = viewModel()

    var contexto by remember { mutableStateOf("TALLER") }
    val userId = SessionManager.getUserId()

    // ⏱️ Reloj en tiempo real
    var tiempoActual by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            tiempoActual = System.currentTimeMillis()
        }
    }

    // 📍 Solicitar permiso de ubicación
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        viewModel.cargarDatos(userId)
    }

    val fichajes = viewModel.fichajesLocales
    val cargando = viewModel.cargando

    // 🔥 Cálculo de tiempos
    val tiempos = calcularTiempos(fichajes)
    var trabajoTotal = tiempos.trabajo
    var viajeTotal = tiempos.viaje
    var descansoTotal = tiempos.descanso

    val ultimo = fichajes.lastOrNull()

    if (ultimo != null) {
        val diff = tiempoActual - ultimo.fecha_hora
        when {
            ultimo.tipo.contains("ENTRADA", true) -> trabajoTotal += diff
            ultimo.tipo.contains("INICIO_VIAJE", true) -> viajeTotal += diff
            ultimo.tipo.contains("INICIO_DESCANSO", true) -> descansoTotal += diff
        }
    }

    // ⏳ Pantalla de carga
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

        // 🔹 Tarjeta de tiempos
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2F5DAA))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "⏱️ Trabajo: ${formatearTiempo(trabajoTotal)}",
                    color = Color.White
                )
                Text(
                    "🚗 Viaje: ${formatearTiempo(viajeTotal)}",
                    color = Color.White
                )
                Text(
                    "☕ Descanso: ${formatearTiempo(descansoTotal)}",
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Selección de contexto
        Text("Selecciona contexto")

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("TALLER", "OBRA", "REPARACION").forEach { item ->
                Button(
                    onClick = { contexto = item },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (contexto == item)
                            MaterialTheme.colorScheme.primary
                        else Color.Gray
                    )
                ) {
                    Text(item)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Acciones")
        Spacer(modifier = Modifier.height(8.dp))

        val acciones = listOf(
            "ENTRADA" to "SALIDA",
            "INICIO_VIAJE" to "FIN_VIAJE",
            "INICIO_DESCANSO" to "FIN_DESCANSO"
        )

        acciones.forEach { (izq, der) ->

            val puedeIzq = viewModel.puedeFichar(izq, contexto)
            val puedeDer = viewModel.puedeFichar(der, contexto)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // 🔹 Botón izquierdo
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

                // 🔹 Botón derecho
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

        // 🔹 Última acción
        viewModel.ultimaAccion?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Text(
                    text = "Última acción: $it",
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }

    // 🔔 Mensajes
    viewModel.mensaje?.let { mensaje ->
        LaunchedEffect(mensaje) {
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            viewModel.mensaje = null
        }
    }
}