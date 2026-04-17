package com.example.programaficharfeoe.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CambiarPasswordScreen() {

    var pass1 by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Cambiar contraseña",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = pass1,
            onValueChange = { pass1 = it },
            label = { Text("Nueva contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = pass2,
            onValueChange = { pass2 = it },
            label = { Text("Repetir contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                mensaje =
                    if (pass1 != pass2)
                        "Las contraseñas no coinciden"
                    else
                        "Contraseña actualizada"
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.Lock, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Actualizar contraseña")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mensaje.isNotBlank()) {
            Text(mensaje)
        }
    }
}