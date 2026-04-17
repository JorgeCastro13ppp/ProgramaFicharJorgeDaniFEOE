package com.example.programaficharfeoe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.screens.login.LoginScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val startDestination =
        if (!SessionManager.getToken().isNullOrEmpty()) {
            AppRoutes.Main.route
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
                    navController.navigate(AppRoutes.Main.route) {
                        popUpTo(AppRoutes.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // MAIN
        composable(AppRoutes.Main.route) {
            MainScreen(
                onLogout = {
                    SessionManager.clearSession()

                    navController.navigate(AppRoutes.Login.route) {
                        popUpTo(AppRoutes.Main.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}