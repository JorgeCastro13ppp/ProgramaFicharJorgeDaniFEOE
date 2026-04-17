package com.example.programaficharfeoe.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.programaficharfeoe.data.local.SessionManager

@Composable
fun PerfilScreen(
    onLogout: () -> Unit
) {
    val username = SessionManager.getUsername() ?: "Usuario"

    var password by remember { mutableStateOf("") }
    var repetir by remember { mutableStateOf("") }

    var mostrarDialogLogout by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        username,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

        Text(
            text = "Cambiar contraseña",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Nueva contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = repetir,
            onValueChange = { repetir = it },
            label = { Text("Repetir contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // luego conectamos backend
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Lock, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cambiar contraseña")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                mostrarDialogLogout = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE57373)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "Cerrar sesión",
                fontWeight = FontWeight.Bold
            )
        }

        if (mostrarDialogLogout) {

            AlertDialog(
                onDismissRequest = {
                    mostrarDialogLogout = false
                },

                containerColor = Color(0xFF1E2746),
                shape = RoundedCornerShape(24.dp),

                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            tint = Color(0xFFE57373)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Cerrar sesión",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },

                text = {
                    Text(
                        text = "¿Seguro que deseas cerrar sesión?",
                        color = Color(0xFFD6D9E0)
                    )
                },

                confirmButton = {
                    Button(
                        onClick = {
                            mostrarDialogLogout = false
                            onLogout()
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE57373)
                        )
                    ) {
                        Text("Sí, salir")
                    }
                },

                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            mostrarDialogLogout = false
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}