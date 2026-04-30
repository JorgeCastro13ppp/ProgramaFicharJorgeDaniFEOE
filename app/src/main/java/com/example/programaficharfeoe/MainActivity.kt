package com.example.programaficharfeoe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.ui.navigation.AppNavigation
import com.example.programaficharfeoe.ui.theme.ProgramaFicharFEOETheme
import com.google.firebase.messaging.FirebaseMessaging
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        SessionManager.init(this)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {

            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }

        setContent {

            ProgramaFicharFEOETheme {

                AppNavigation()
            }
        }

        enviarTokenSiExisteSesion()
    }

    private fun enviarTokenSiExisteSesion() {

        val jwt = SessionManager.getToken() ?: return

        FirebaseMessaging
            .getInstance()
            .token
            .addOnSuccessListener { token ->

                Log.d("FCM", "Token actual: $token")

                enviarTokenAlBackend(token, jwt)
            }
    }

    private fun enviarTokenAlBackend(
        token: String,
        jwt: String
    ) {

        val body =
            """
            {
                "token": "$token",
                "platform": "android"
            }
            """.trimIndent()

        Thread {

            try {

                val url =
                    URL(
                        "https://192.168.1.45:8443/device/register"
                    )

                val conn =
                    url.openConnection()
                            as HttpURLConnection

                conn.requestMethod = "POST"

                conn.setRequestProperty(
                    "Content-Type",
                    "application/json"
                )

                conn.setRequestProperty(
                    "Authorization",
                    "Bearer $jwt"
                )

                conn.doOutput = true

                conn.outputStream.use {

                    it.write(body.toByteArray())
                }

                Log.d(
                    "FCM",
                    "Token enviado correctamente (MainActivity)"
                )

            } catch (e: Exception) {

                Log.e(
                    "FCM",
                    "Error enviando token desde MainActivity",
                    e
                )
            }

        }.start()
    }
}