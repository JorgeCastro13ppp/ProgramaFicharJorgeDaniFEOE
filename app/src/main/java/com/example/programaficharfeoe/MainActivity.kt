package com.example.programaficharfeoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.navigation.AppNavigation
import com.example.programaficharfeoe.ui.theme.ProgramaFicharFEOETheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Inicializar sesión
        SessionManager.init(this)

        setContent {
            ProgramaFicharFEOETheme {
                AppNavigation()
            }
        }
    }
}