package com.example.programaficharfeoe.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun appShimmerBrush(): Brush {

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

    return Brush.linearGradient(

        colors = listOf(

            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),

            MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),

            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        ),

        start = Offset(
            translateAnim - 300f,
            translateAnim - 300f
        ),

        end = Offset(
            translateAnim,
            translateAnim
        )
    )
}

@Composable
fun LoadingView() {

    val brush = appShimmerBrush()

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(14.dp)

    ) {

        repeat(4) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(brush)
            )
        }
    }
}