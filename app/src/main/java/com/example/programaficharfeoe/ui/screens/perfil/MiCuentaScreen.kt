package com.example.programaficharfeoe.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.programaficharfeoe.data.local.SessionManager

@Composable
fun MiCuentaScreen() {

    val username = SessionManager.getUsername() ?: "Usuario"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Mi cuenta",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {

                Text(
                    text = username,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text("HIDROCAEX")
                Text("Empleado activo")
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        CuentaItem(Icons.Default.Badge, "ID usuario", "Interno")
        CuentaItem(Icons.Default.Business, "Empresa", "HIDROCAEX")
        CuentaItem(Icons.Default.Lock, "Seguridad", "Contraseña protegida")
        CuentaItem(Icons.Default.Update, "Versión", "1.0.0")
        CuentaItem(Icons.Default.PhoneAndroid, "Dispositivo", "Android")
    }
}

@Composable
fun CuentaItem(
    icon: ImageVector,
    titulo: String,
    subtitulo: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(icon, null)

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}