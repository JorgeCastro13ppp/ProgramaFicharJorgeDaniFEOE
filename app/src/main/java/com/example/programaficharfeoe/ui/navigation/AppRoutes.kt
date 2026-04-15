package com.example.programaficharfeoe.ui.navigation

sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object Home : AppRoutes("home")
    object ContextSelection : AppRoutes("context_selection")
    object Fichaje : AppRoutes("fichaje")
    object Vacaciones : AppRoutes("vacaciones")
    object Faltas : AppRoutes("faltas")

    object Nominas : AppRoutes("nominas")
    object Reconocimientos : AppRoutes("reconocimientos")
    object Formacion : AppRoutes("formacion")
    object Epis : AppRoutes("epis")

    object FichajeConContexto : AppRoutes("fichaje/{contexto}") {
        fun createRoute(contexto: String) = "fichaje/$contexto"
    }
}