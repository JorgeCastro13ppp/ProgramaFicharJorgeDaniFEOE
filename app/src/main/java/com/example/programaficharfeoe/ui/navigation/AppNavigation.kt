package com.example.programaficharfeoe.ui.navigation

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.screens.login.LoginScreen
import com.example.programaficharfeoe.ui.screens.home.HomeScreen
import com.example.programaficharfeoe.ui.screens.faltas.FaltasScreen
import com.example.programaficharfeoe.ui.screens.fichaje.FichajeScreen
import com.example.programaficharfeoe.ui.screens.qr.QRScreen
import com.example.programaficharfeoe.ui.screens.vacaciones.VacacionesScreen
import com.example.programaficharfeoe.ui.screens.nominas.NominasScreen
import com.example.programaficharfeoe.ui.screens.reconocimientos.ReconocimientosScreen
import com.example.programaficharfeoe.ui.screens.formacion.FormacionesScreen
import com.example.programaficharfeoe.ui.screens.epis.EpisScreen

@OptIn(ExperimentalGetImage::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // 🔥 decidir pantalla inicial
    val startDestination = if (sessionManager.getToken() != null) {
        "home"
    } else {
        "login"
    }

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
                onGoToFaltas = { navController.navigate("faltas") },
                onGoToVacaciones = { navController.navigate("vacaciones") },
                onGoToNominas = { navController.navigate("nominas") },
                onGoToReconocimientos = { navController.navigate("reconocimientos") },
                onGoToFormacion = { navController.navigate("formacion") },
                onGoToEpis = { navController.navigate("epis") },
                onLogout = {
                    sessionManager.clearSession()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // 📌 PANTALLAS

        composable("faltas") { FaltasScreen() }

        composable("fichaje") { FichajeScreen() }

        composable("qr") { QRScreen() }

        composable("vacaciones") { VacacionesScreen() }

        composable("nominas") { NominasScreen() }

        composable("reconocimientos") { ReconocimientosScreen() }

        composable("formacion") { FormacionesScreen() }

        composable("epis") { EpisScreen() }
    }
}