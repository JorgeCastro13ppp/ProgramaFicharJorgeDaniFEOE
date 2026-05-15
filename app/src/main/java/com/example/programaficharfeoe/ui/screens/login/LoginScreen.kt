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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.expandVertically
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel()

    val state = viewModel.uiState

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        delay(150)

        visible = true
    }

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
                Brush.linearGradient(
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
            val infiniteTransition = rememberInfiniteTransition(
                label = ""
            )

            val logoScale by infiniteTransition.animateFloat(

                initialValue = 1f,

                targetValue = 1.04f,

                animationSpec = infiniteRepeatable(

                    animation = tween(1800),

                    repeatMode = RepeatMode.Reverse
                ),

                label = ""
            )

            AnimatedVisibility(

                visible = visible,

                enter = fadeIn(
                    animationSpec = tween(700)
                ) + slideInVertically(

                    initialOffsetY = { -80 },

                    animationSpec = tween(700)
                )
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logoplantilla),
                    contentDescription = "Logo empresa",

                    modifier = Modifier
                        .size(160.dp)
                        .scale(logoScale)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // TARJETA LOGIN
            AnimatedVisibility(

                visible = visible,

                enter = fadeIn(
                    animationSpec = tween(900)
                ) + slideInVertically(

                    initialOffsetY = { 120 },

                    animationSpec = tween(900)
                ) + expandVertically(

                    animationSpec = tween(900)
                )
            ) {

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

                    AnimatedVisibility(

                        visible = state.error != null,

                        enter = fadeIn(
                            animationSpec = tween(300)
                        ) + expandVertically(

                            animationSpec = tween(300)
                        )
                    ) {

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
    }}}
}