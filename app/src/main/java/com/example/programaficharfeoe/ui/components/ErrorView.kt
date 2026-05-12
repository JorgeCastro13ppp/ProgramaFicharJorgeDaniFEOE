package com.example.programaficharfeoe.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorView(

    message: String,

    modifier: Modifier = Modifier,

    onRetry: (() -> Unit)? = null
) {

    Column(

        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(

            text = message,

            color = MaterialTheme.colorScheme.error,

            textAlign = TextAlign.Center
        )

        if (onRetry != null) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onRetry
            ) {

                Text("Reintentar")
            }
        }
    }
}