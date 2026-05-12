package com.example.programaficharfeoe.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.programaficharfeoe.R
import com.example.programaficharfeoe.viewmodel.LoginViewModel
import com.example.programaficharfeoe.ui.components.AppButton
import com.example.programaficharfeoe.ui.components.ErrorView

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel()

    val state = viewModel.uiState

    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) {
            onLoginSuccess()
        }
    }

    // Fondo azul degradado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.primary
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            // LOGO GRANDE
            Image(
                painter = painterResource(id = R.drawable.logoplantilla),
                contentDescription = "Logo empresa",
                modifier = Modifier.size(160.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // TARJETA LOGIN
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .padding(24.dp)
                ) {

                    Text(
                        text = "Iniciar sesión",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = state.username,
                        onValueChange = {
                            viewModel.onUsernameChange(it)
                            viewModel.limpiarError()
                        },
                        label = { Text("Usuario") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.password,
                        onValueChange = {
                            viewModel.onPasswordChange(it)
                            viewModel.limpiarError()
                        },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    AppButton(

                        text = "Entrar",

                        onClick = {
                            viewModel.login()
                        },

                        isLoading = state.isLoading
                    )

                    state.error?.let { error ->

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        ErrorView(
                            message = error,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}