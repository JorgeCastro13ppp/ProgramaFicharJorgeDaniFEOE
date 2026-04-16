package com.example.programaficharfeoe.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.programaficharfeoe.ui.screens.documentos.DocumentosMenuScreen
import com.example.programaficharfeoe.ui.screens.documentos.DocumentosScreen
import com.example.programaficharfeoe.ui.screens.faltas.FaltasScreen
import com.example.programaficharfeoe.ui.screens.fichaje.FichajeScreen
import com.example.programaficharfeoe.ui.screens.vacaciones.VacacionesScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Fichaje.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            // 🔹 FICHAJE
            composable(BottomNavItem.Fichaje.route) {
                FichajeScreen()
            }

            // 🔹 VACACIONES
            composable(BottomNavItem.Vacaciones.route) {
                VacacionesScreen()
            }

            // 🔹 FALTAS
            composable(BottomNavItem.Faltas.route) {
                FaltasScreen()
            }

            // 🔹 DOCUMENTOS
            composable(BottomNavItem.Documentos.route) {
                DocumentosMenuScreen(navController)
            }

            composable(
                route = "documentos/{tipo}/{titulo}"
            ) { backStackEntry ->
                val tipo = backStackEntry.arguments?.getString("tipo") ?: ""
                val titulo = backStackEntry.arguments?.getString("titulo") ?: "Documentos"

                DocumentosScreen(
                    titulo = titulo,
                    tipo = tipo
                )
            }
        }
    }
}