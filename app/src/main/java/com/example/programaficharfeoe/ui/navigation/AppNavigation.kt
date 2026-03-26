package com.example.programaficharfeoe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.screens.home.HomeScreen
import com.example.programaficharfeoe.ui.screens.login.LoginScreen
import com.example.programaficharfeoe.ui.screens.fichaje.FichajeScreen
import com.example.programaficharfeoe.ui.screens.vacaciones.VacacionesScreen
import com.example.programaficharfeoe.ui.screens.faltas.FaltasScreen
import com.example.programaficharfeoe.ui.screens.documentos.DocumentosScreen

@Composable
fun AppNavigation(
    onScanQR: (String) -> Unit
) {

    val navController = rememberNavController()
    val context = LocalContext.current

    // Inicializar sesión
    SessionManager.init(context)

    val startDestination =
        if (SessionManager.getToken() != null) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // LOGIN
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // HOME
        composable("home") {
            HomeScreen(
                onNavigate = { ruta ->
                    navController.navigate(ruta)
                },
                onLogout = {
                    SessionManager.clearSession()

                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // FICHAJE
        composable("fichaje") {
            FichajeScreen(
                onScanQR = onScanQR
            )
        }

        // VACACIONES
        composable("vacaciones") {
            VacacionesScreen()
        }

        // FALTAS
        composable("faltas") {
            FaltasScreen()
        }

        // DOCUMENTOS
        composable("nominas") {
            DocumentosScreen("Nóminas", "nomina")
        }

        composable("epis") {
            DocumentosScreen("EPIs", "epi")
        }

        composable("formacion") {
            DocumentosScreen("Formación", "formacion")
        }

        composable("reconocimientos") {
            DocumentosScreen("Reconocimientos", "reconocimiento")
        }
    }
}