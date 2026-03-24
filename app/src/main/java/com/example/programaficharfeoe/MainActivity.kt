package com.example.programaficharfeoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.data.remote.RetrofitClient
import com.example.programaficharfeoe.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(this)
        val token = sessionManager.getToken()

        if (token != null) {
            RetrofitClient.setToken(token)
        }

        setContent {
            AppNavigation()
        }
    }
}