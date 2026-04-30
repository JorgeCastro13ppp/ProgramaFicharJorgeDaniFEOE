package com.example.programaficharfeoe.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.di.AppModule
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class LoginViewModel : ViewModel() {

    private val repository = AppModule.authRepository

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var loginResult by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set


    fun login() {

        viewModelScope.launch {

            isLoading = true

            repository.login(username, password)
                .onSuccess {

                    SessionManager.saveToken(it.token)
                    SessionManager.saveUsername(username)
                    SessionManager.saveUserId(it.userId)

                    enviarTokenFCMAlBackend(it.token)

                    loginResult = "OK"
                }

                .onFailure {

                    loginResult = "ERROR"
                }

            isLoading = false
        }
    }


    private fun enviarTokenFCMAlBackend(jwt: String) {

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { fcmToken ->

                Log.d("FCM_DEBUG", "Token obtenido: $fcmToken")

                viewModelScope.launch(Dispatchers.IO) {

                    try {

                        val url =
                            URL("https://192.168.1.45:8443/device/register")

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

                        val body =
                            """
                        {
                            "token": "$fcmToken",
                            "platform": "android"
                        }
                        """.trimIndent()

                        conn.outputStream.use {

                            it.write(body.toByteArray())
                        }

                        Log.d("FCM_DEBUG", "Response code: ${conn.responseCode}")


                        Log.d(
                            "FCM_DEBUG",
                            "Token enviado correctamente al backend"
                        )

                    } catch (e: Exception) {

                        Log.e(
                            "FCM_DEBUG",
                            "Error enviando token",
                            e
                        )
                    }
                }
            }
    }}