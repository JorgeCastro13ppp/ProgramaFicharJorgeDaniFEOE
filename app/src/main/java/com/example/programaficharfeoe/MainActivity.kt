package com.example.programaficharfeoe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.data.remote.RetrofitInstance
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

        RetrofitInstance.init(this)

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
    }
}