package com.example.programaficharfeoe.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.programaficharfeoe.data.local.SessionManager

@Composable
fun HomeScreen(
    onIrFichaje: () -> Unit,
    onIrVacaciones: () -> Unit,
    onIrDocumentos: () -> Unit
) {

    val username = SessionManager.getUsername() ?: "Usuario"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {

        // HEADER
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
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
                    .padding(22.dp)
            ) {

                Column {
                    Text(
                        text = "Buenos días 👋",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = username,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Programa Fichar FEOE",
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // HORAS HOY
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = Color(0xFF2563EB)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        "Hoy llevas",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "6h 22m trabajadas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ÚLTIMO FICHAJE
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.WorkHistory,
                    contentDescription = null,
                    tint = Color(0xFF22C55E)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        "Último fichaje",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "ENTRADA TALLER · 08:01",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // RESUMEN SEMANA
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Resumen semanal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                WeekRow("Lunes", "8h")
                WeekRow("Martes", "8h")
                WeekRow("Miércoles", "7h")
                WeekRow("Jueves", "8h")
                WeekRow("Viernes", "6h")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        Icon(icon, null)

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun WeekRow(
    day: String,
    hours: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(day)
        Text(
            hours,
            fontWeight = FontWeight.Bold
        )
    }
}