package com.example.programaficharfeoe.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale
import androidx.compose.runtime.getValue
import androidx.compose.foundation.clickable

@Composable
fun AppCard(

    modifier: Modifier = Modifier,

    onClick: (() -> Unit)? = null,

    content: @Composable ColumnScope.() -> Unit
) {

    val isDark = isSystemInDarkTheme()

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(

        targetValue = if (isPressed)
            0.98f
        else
            1f,

        animationSpec = tween(120),

        label = ""
    )

    Card(

        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .then(

                if (onClick != null) {

                    Modifier.clickable(

                        interactionSource = interactionSource,

                        indication = null

                    ) {

                        onClick()
                    }

                } else {

                    Modifier
                }
            ),

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(

            containerColor = if (isDark)

                MaterialTheme.colorScheme.surfaceVariant
                    .copy(alpha = 0.4f)

            else

                MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(

            defaultElevation = if (isDark)
                0.dp
            else
                3.dp
        ),

        content = content
    )
}