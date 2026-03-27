package com.example.programaficharfeoe

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.data.repository.FichajeRepository
import com.example.programaficharfeoe.ui.navigation.AppNavigation
import com.example.programaficharfeoe.ui.theme.ProgramaFicharFEOETheme
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.example.programaficharfeoe.ui.theme.AzulHidrocaex

class MainActivity : ComponentActivity() {

    private var tipoActual: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar sesión
        SessionManager.init(this)

        setContent {
            ProgramaFicharFEOETheme {

                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = AzulHidrocaex
                    )
                }

                AppNavigation()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {

            if (result.contents != null) {

                val qrResult = result.contents
                val tipo = tipoActual ?: "entrada"

                val repo = FichajeRepository()

                CoroutineScope(Dispatchers.IO).launch {

                    val ok = repo.fichar(qrResult, tipo)

                    runOnUiThread {
                        if (ok) {
                            Toast.makeText(
                                this@MainActivity,
                                "Fichaje de $tipo realizado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Error al fichar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            } else {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}