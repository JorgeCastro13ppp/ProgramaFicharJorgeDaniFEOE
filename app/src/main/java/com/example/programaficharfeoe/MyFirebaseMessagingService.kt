package com.example.programaficharfeoe

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.programaficharfeoe.data.local.SessionManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.HttpURLConnection
import java.net.URL
import android.app.PendingIntent
import android.content.Intent
import com.example.programaficharfeoe.utils.Constants

class MyFirebaseMessagingService :
    FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d("FCM_DEBUG", "MENSAJE RECIBIDO COMPLETO")

        val title =
            remoteMessage.data["title"]
                ?: remoteMessage.notification?.title ?: ""

        val body =
            remoteMessage.data["body"]
                ?: remoteMessage.notification?.body ?: ""

        Log.d("FCM_DEBUG", "Título: $title")
        Log.d("FCM_DEBUG", "Mensaje: $body")

        mostrarNotificacion(title, body)
    }


    override fun onNewToken(token: String) {

        Log.d("FCM", "Nuevo token: $token")

        // 🔹 GUARDAR SIEMPRE EL TOKEN
        SessionManager.saveFcmToken(token)

        // 🔹 SI HAY SESIÓN → enviarlo
        val jwt = SessionManager.getToken()

        if (jwt != null) {
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
                    URL("${Constants.BASE_URL}/device/register")

                val conn =
                    url.openConnection() as HttpURLConnection

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

                Log.d("FCM", "Token enviado correctamente al backend")

            } catch (e: Exception) {

                Log.e("FCM", "Error enviando token", e)
            }

        }.start()
    }


    private fun mostrarNotificacion(
        title: String,
        message: String
    ) {

        val channelId = "faltas_channel"

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE)
                    as NotificationManager

        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "Notificaciones Faltas",
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager.createNotificationChannel(channel)
        }


        val notification =
            NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()


        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}