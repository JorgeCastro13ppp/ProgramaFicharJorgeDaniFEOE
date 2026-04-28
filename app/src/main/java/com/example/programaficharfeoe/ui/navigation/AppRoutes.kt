package com.example.programaficharfeoe.ui.navigation

sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object Main : AppRoutes("main")

    object Fichaje : AppRoutes("fichaje")
}