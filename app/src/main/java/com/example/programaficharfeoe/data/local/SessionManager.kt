package com.example.programaficharfeoe.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SessionManager {

    private const val PREF_NAME = "app_session"
    private const val KEY_TOKEN = "token"
    private const val KEY_USERNAME = "username"
    private const val KEY_USER_ID = "user_id"

    private const val KEY_FCM_TOKEN = "fcm_token"

    private lateinit var prefs: SharedPreferences

    fun saveFcmToken(token: String) {
        edit { it.putString(KEY_FCM_TOKEN, token) }
    }

    fun init(context: Context) {

        if (!::prefs.isInitialized) {

            val appContext = context.applicationContext

            val masterKey = MasterKey.Builder(
                appContext
            )
                .setKeyScheme(
                    MasterKey.KeyScheme.AES256_GCM
                )
                .build()

            prefs = EncryptedSharedPreferences.create(
                appContext,
                PREF_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    private fun checkInitialization() {
        if (!::prefs.isInitialized) {
            throw IllegalStateException(
                "SessionManager no ha sido inicializado. Llama a init(context) en MainActivity."
            )
        }
    }

    private inline fun edit(operation: (SharedPreferences.Editor) -> Unit) {
        checkInitialization()
        val editor = prefs.edit()
        operation(editor)
        editor.apply()
    }

    fun saveToken(token: String) {
        edit { it.putString(KEY_TOKEN, token) }
    }

    fun getToken(): String? {
        checkInitialization()
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUsername(username: String) {
        edit { it.putString(KEY_USERNAME, username) }
    }

    fun getUsername(): String? {
        checkInitialization()
        return prefs.getString(KEY_USERNAME, null)
    }

    fun saveUserId(userId: Int) {
        edit { it.putInt(KEY_USER_ID, userId) }
    }

    fun getUserId(): Int? {
        checkInitialization()
        val id = prefs.getInt(KEY_USER_ID, -1)
        return if (id == -1) null else id
    }

    fun clearSession() {

        try {

            checkInitialization()

            prefs.edit()
                .remove(KEY_TOKEN)
                .remove(KEY_USERNAME)
                .remove(KEY_USER_ID)
                .remove(KEY_FCM_TOKEN)
                .apply()

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}