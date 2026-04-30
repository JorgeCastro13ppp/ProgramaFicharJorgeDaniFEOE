package com.example.programaficharfeoe

import android.util.Log
import com.example.programaficharfeoe.data.local.SessionManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService :
    FirebaseMessagingService() {

    override fun onMessageReceived(
        remoteMessage: RemoteMessage
    ) {

        remoteMessage.notification?.let {

            Log.d("FCM", "Título: ${it.title}")
            Log.d("FCM", "Mensaje: ${it.body}")
        }
    }

    override fun onNewToken(token: String) {

        Log.d("FCM", "Nuevo token: $token")

        enviarTokenAlBackend(token)
    }

    private fun enviarTokenAlBackend(token: String) {

        val jwt =
            SessionManager.getToken()

        if (jwt == null) {

            Log.d(
                "FCM",
                "Token generado pero usuario no logueado aún"
            )

            return
        }

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
                    "Token enviado correctamente al backend"
                )

            } catch (e: Exception) {

                Log.e(
                    "FCM",
                    "Error enviando token al backend",
                    e
                )
            }

        }.start()
    }
}