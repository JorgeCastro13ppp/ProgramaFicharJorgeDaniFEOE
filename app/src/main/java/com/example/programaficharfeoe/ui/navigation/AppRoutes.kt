package com.example.programaficharfeoe.ui.navigation

sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object Main : AppRoutes("main")

    object Fichaje : AppRoutes("fichaje")
    object Vacaciones : AppRoutes("vacaciones")
    object Faltas : AppRoutes("faltas")
    object Documentos : AppRoutes("documentos")

    object Nominas : AppRoutes("nominas")
    object Reconocimientos : AppRoutes("reconocimientos")
    object Formacion : AppRoutes("formacion")
    object Epis : AppRoutes("epis")
}