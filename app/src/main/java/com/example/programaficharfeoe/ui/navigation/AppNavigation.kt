package com.example.programaficharfeoe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.screens.documentos.DocumentosScreen
import com.example.programaficharfeoe.ui.screens.faltas.FaltasScreen
import com.example.programaficharfeoe.ui.screens.fichaje.FichajeScreen
import com.example.programaficharfeoe.ui.screens.home.HomeScreen
import com.example.programaficharfeoe.ui.screens.login.LoginScreen
import com.example.programaficharfeoe.ui.screens.vacaciones.VacacionesScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (SessionManager.getToken() != null) "home" else "login"
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
            FichajeScreen(navController = navController)
        }

        // VACACIONES
        composable("vacaciones") {
            VacacionesScreen()
        }

        // DOCUMENTOS (NÓMINAS, EPIS, ETC)
        composable("nominas") {
            DocumentosScreen("Nóminas", "nomina")
        }

        composable("reconocimientos") {
            DocumentosScreen("Reconocimientos", "reconocimiento")
        }

        composable("formacion") {
            DocumentosScreen("Formación", "formacion")
        }

        composable("epis") {
            DocumentosScreen("EPIs", "epi")
        }

        // FALTAS
        composable("faltas") {
            FaltasScreen()
        }
    }
}