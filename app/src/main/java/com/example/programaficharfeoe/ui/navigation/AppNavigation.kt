package com.example.programaficharfeoe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.programaficharfeoe.ui.screens.login.LoginScreen
import com.example.programaficharfeoe.ui.screens.home.HomeScreen
import com.example.programaficharfeoe.ui.screens.fichaje.FichajeScreen
import com.example.programaficharfeoe.ui.screens.faltas.FaltasScreen
import com.example.programaficharfeoe.ui.screens.vacaciones.VacacionesScreen
import com.example.programaficharfeoe.ui.screens.documentos.DocumentosScreen
import com.example.programaficharfeoe.data.local.SessionManager

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

        // 🔐 LOGIN
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 🏠 HOME
        composable("home") {
            HomeScreen(
                onGoToFichaje = { navController.navigate("fichaje") },
                onGoToVacaciones = { navController.navigate("vacaciones") },
                onGoToNominas = { navController.navigate("nominas") },
                onGoToFaltas = { navController.navigate("faltas") },
                onGoToReconocimientos = { navController.navigate("reconocimientos") },
                onGoToFormacion = { navController.navigate("formacion") },
                onGoToEpis = { navController.navigate("epis") },
                onLogout = {
                    SessionManager.clearSession()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // 📷 FICHAJE QR
        composable("fichaje") {
            FichajeScreen(
                onScanQR = { tipo ->
                    onScanQR(tipo)
                }
            )
        }

        // 📄 DOCUMENTOS (GENÉRICO)
        composable("nominas") {
            DocumentosScreen(
                tipo = "nomina",
                titulo = "Nóminas"
            )
        }

        composable("reconocimientos") {
            DocumentosScreen(
                tipo = "reconocimiento",
                titulo = "Reconocimientos"
            )
        }

        composable("formacion") {
            DocumentosScreen(
                tipo = "formacion",
                titulo = "Formación"
            )
        }

        composable("epis") {
            DocumentosScreen(
                tipo = "epi",
                titulo = "EPIs"
            )
        }

        // 📉 FALTAS
        composable("faltas") {
            FaltasScreen()
        }

        // 🌴 VACACIONES
        composable("vacaciones") {
            VacacionesScreen()
        }
    }
}