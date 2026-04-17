package com.example.programaficharfeoe.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Fichaje : BottomNavItem("fichaje", "Fichaje", Icons.Default.AccessTime)
    object Vacaciones : BottomNavItem("vacaciones", "Vacaciones", Icons.Default.BeachAccess)
    object Faltas : BottomNavItem("faltas", "Faltas", Icons.Default.EventBusy)
    object Documentos : BottomNavItem("documentos", "Documentos", Icons.Default.Description)

    object Perfil : BottomNavItem("perfil", "Perfil", Icons.Default.Person)


}