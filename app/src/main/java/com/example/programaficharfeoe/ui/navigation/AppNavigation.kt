package com.example.programaficharfeoe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.screens.context.ContextSelectionScreen
import com.example.programaficharfeoe.ui.screens.documentos.DocumentosScreen
import com.example.programaficharfeoe.ui.screens.faltas.FaltasScreen
import com.example.programaficharfeoe.ui.screens.fichaje.FichajeScreen
import com.example.programaficharfeoe.ui.screens.home.HomeScreen
import com.example.programaficharfeoe.ui.screens.login.LoginScreen
import com.example.programaficharfeoe.ui.screens.vacaciones.VacacionesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val startDestination = if (!SessionManager.getToken().isNullOrEmpty()) {
        AppRoutes.Home.route
    } else {
        AppRoutes.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // LOGIN
        composable(AppRoutes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // HOME
        composable(AppRoutes.Home.route) {
            HomeScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onLogout = {
                    SessionManager.clearSession()
                    navController.navigate(AppRoutes.Login.route) {
                        popUpTo(AppRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // SELECCIÓN DE CONTEXTO
        composable(AppRoutes.ContextSelection.route) {
            ContextSelectionScreen(navController)
        }

        // FICHAJE (redirige a selección de contexto)
        composable(AppRoutes.Fichaje.route) {
            ContextSelectionScreen(navController)
        }

        // FICHAJE CON CONTEXTO
        composable(
            route = AppRoutes.FichajeConContexto.route,
            arguments = listOf(
                navArgument("contexto") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val contexto =
                backStackEntry.arguments?.getString("contexto") ?: "TALLER"

            FichajeScreen(
                contextoInicial = contexto,
                onBack = { navController.popBackStack() }
            )
        }

        // VACACIONES
        composable(AppRoutes.Vacaciones.route) {
            VacacionesScreen()
        }

        // DOCUMENTOS
        composable(AppRoutes.Nominas.route) {
            DocumentosScreen("Nóminas", "nomina")
        }

        composable(AppRoutes.Reconocimientos.route) {
            DocumentosScreen("Reconocimientos", "reconocimiento")
        }

        composable(AppRoutes.Formacion.route) {
            DocumentosScreen("Formación", "formacion")
        }

        composable(AppRoutes.Epis.route) {
            DocumentosScreen("EPIs", "epis")
        }

        // FALTAS
        composable(AppRoutes.Faltas.route) {
            FaltasScreen()
        }
    }
}