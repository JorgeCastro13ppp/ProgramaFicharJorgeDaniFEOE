package com.example.programaficharfeoe.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    suspend fun saveLogin() {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = true
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = false
        }
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            prefs[IS_LOGGED_IN] ?: false
        }
}