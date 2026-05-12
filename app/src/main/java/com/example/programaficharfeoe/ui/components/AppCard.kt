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

@Composable
fun AppCard(

    modifier: Modifier = Modifier,

    content: @Composable ColumnScope.() -> Unit
) {

    val isDark = isSystemInDarkTheme()

    Card(

        modifier = modifier.fillMaxWidth(),

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