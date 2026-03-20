package com.example.programaficharfeoe.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.screens.home.HomeScreen
import com.example.programaficharfeoe.ui.screens.login.LoginScreen
import com.example.programaficharfeoe.ui.screens.qr.QRScreen
import com.example.programaficharfeoe.ui.screens.vacaciones.VacacionesScreen
import com.example.programaficharfeoe.ui.screens.nominas.NominasScreen
import com.example.programaficharfeoe.ui.screens.faltas.FaltasScreen
import com.example.programaficharfeoe.ui.screens.reconocimientos.ReconocimientosScreen
import com.example.programaficharfeoe.ui.screens.formacion.FormacionScreen
import com.example.programaficharfeoe.ui.screens.epis.EpisScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current

    var startDestination by remember { mutableStateOf<String?>(null) }

    // 🔐 Comprobar sesión
    LaunchedEffect(Unit) {
        val sessionManager = SessionManager(context)
        val isLogged = sessionManager.isLoggedIn.first()
        startDestination = if (isLogged) "home" else "login"
    }

    if (startDestination == null) return

    NavHost(
        navController = navController,
        startDestination = startDestination!!
    ) {

        // 🔹 LOGIN
        composable("login") {
            LoginScreen(
                onLoginSuccess = {

                    val sessionManager = SessionManager(context)

                    CoroutineScope(Dispatchers.IO).launch {
                        sessionManager.saveLogin()
                    }

                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 🔹 HOME
        composable("home") {
            HomeScreen(
                onGoToFichaje = { navController.navigate("qr") },
                onGoToVacaciones = { navController.navigate("vacaciones") },
                onGoToNominas = { navController.navigate("nominas") },
                onGoToFaltas = { navController.navigate("faltas") },
                onGoToReconocimientos = { navController.navigate("reconocimientos") },
                onGoToFormacion = { navController.navigate("formacion") },
                onGoToEpis = { navController.navigate("epis") },
                onLogout = {

                    val sessionManager = SessionManager(context)

                    CoroutineScope(Dispatchers.IO).launch {
                        sessionManager.logout()
                    }

                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // 🔹 QR
        composable("qr") { QRScreen() }

        // 🔹 VACACIONES
        composable("vacaciones") { VacacionesScreen() }

        // 🔹 NÓMINAS
        composable("nominas") { NominasScreen() }

        // 🔹 FALTAS
        composable("faltas") { FaltasScreen() }

        // 🔹 RECONOCIMIENTOS
        composable("reconocimientos") { ReconocimientosScreen() }

        // 🔹 FORMACIÓN
        composable("formacion") { FormacionScreen() }

        // 🔹 EPIs
        composable("epis") { EpisScreen() }
    }
}