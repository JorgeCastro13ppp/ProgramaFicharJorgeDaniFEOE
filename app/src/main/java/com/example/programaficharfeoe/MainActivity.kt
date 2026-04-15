package com.example.programaficharfeoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.navigation.AppNavigation
import com.example.programaficharfeoe.ui.theme.ProgramaFicharFEOETheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el gestor de sesión con el contexto de la aplicación
        SessionManager.init(applicationContext)

        setContent {
            ProgramaFicharFEOETheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}