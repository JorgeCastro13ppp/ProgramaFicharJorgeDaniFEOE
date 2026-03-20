package com.example.programaficharfeoe.ui.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Home : Routes("home")
    object Fichaje : Routes("fichaje")
}