package com.example.programaficharfeoe.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun shimmerBrush(): Brush {

    val transition = rememberInfiniteTransition(
        label = ""
    )

    val translateAnim by transition.animateFloat(

        initialValue = 0f,

        targetValue = 1000f,

        animationSpec = infiniteRepeatable(

            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),

            repeatMode = RepeatMode.Restart
        ),

        label = ""
    )

    val colors = listOf(

        MaterialTheme.colorScheme.surfaceVariant
            .copy(alpha = 0.9f),

        MaterialTheme.colorScheme.surfaceVariant
            .copy(alpha = 0.3f),

        MaterialTheme.colorScheme.surfaceVariant
            .copy(alpha = 0.9f)
    )

    return Brush.linearGradient(

        colors = colors,

        start = Offset.Zero,

        end = Offset(
            x = translateAnim,
            y = translateAnim
        )
    )
}

@Composable
fun ShimmerDocumentItem() {

    val shimmer = shimmerBrush()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(
                shimmer,
                RoundedCornerShape(18.dp)
            )
    )
}