package com.example.programaficharfeoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale

@Composable
fun FichajeHeader(
    estadoActual: String?
) {

    val colorEstado = when {

        estadoActual?.contains("ENTRADA") == true ->
            Color(0xFF22C55E)

        estadoActual?.contains("PAUSA") == true ->
            Color(0xFFF59E0B)

        else ->
            Color.LightGray
    }

    val infiniteTransition = rememberInfiniteTransition(
        label = ""
    )

    val scaleAnim by infiniteTransition.animateFloat(

        initialValue = 1f,

        targetValue = 1.25f,

        animationSpec = infiniteRepeatable(

            animation = tween(
                durationMillis = 900
            ),

            repeatMode = RepeatMode.Reverse
        ),

        label = ""
    )

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.AccessTime,
                    null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .scale(scaleAnim)
                                .background(
                                    colorEstado,
                                    CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = estadoActual ?: "Sin estado",
                            color = Color.White.copy(alpha = 0.95f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Fichaje",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Acciones disponibles",
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}