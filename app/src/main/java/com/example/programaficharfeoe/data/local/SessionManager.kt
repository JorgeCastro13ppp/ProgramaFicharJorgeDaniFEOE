package com.example.programaficharfeoe.data.local

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}