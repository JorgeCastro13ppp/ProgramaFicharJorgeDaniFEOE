package com.example.programaficharfeoe.ui.screens.fichaje

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
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
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.viewmodel.FichajeViewModel
import kotlinx.coroutines.delay

@Composable
fun FichajeScreen(
    onSuccess: () -> Unit
) {

    val context = LocalContext.current
    val viewModel: FichajeViewModel = viewModel()

    var contexto by remember { mutableStateOf("TALLER") }
    var mostrarExito by remember { mutableStateOf(false) }

    // Permiso ubicación (solo pedir, no gestionar aquí)
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Fichaje",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CONTEXTO
        Text("Selecciona contexto")

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("TALLER", "OBRA", "REPARACION").forEach { contextoItem ->

                Button(

                    onClick = { contexto = contextoItem },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (contexto == contextoItem)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Gray
                    )
                ) {
                    Text(contextoItem)
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

        acciones.forEach { (izquierda, derecha) ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // IZQUIERDA
                Button(
                    onClick = {
                        val userId = SessionManager.getUserId()

                        viewModel.fichar(
                            context = context,
                            userId = userId,
                            contexto = contexto,
                            accion = izquierda
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(izquierda.replace("_", " "))
                }

                // DERECHA
                Button(
                    onClick = {
                        val userId = SessionManager.getUserId()

                        viewModel.fichar(
                            context = context,
                            userId = userId,
                            contexto = contexto,
                            accion = derecha
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(derecha.replace("_", " "))
                }
            }
        }
    }

    // RESPUESTA
    viewModel.mensaje?.let { mensaje ->

        LaunchedEffect(mensaje) {
            Toast.makeText(context, "Respuesta: $mensaje", Toast.LENGTH_LONG).show()

            if (!mensaje.contains("Error", true)) {

                val vibrator =
                    context.getSystemService(Vibrator::class.java)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    vibrator.vibrate(200)
                }

                mostrarExito = true

                delay(1200)

                viewModel.mensaje = null
                mostrarExito = false

                onSuccess()

            } else {
                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                viewModel.mensaje = null
            }
        }
    }

    // ÉXITO
    AnimatedVisibility(
        visible = mostrarExito,
        enter = fadeIn()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA4CAF50)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✔ Fichaje correcto",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}