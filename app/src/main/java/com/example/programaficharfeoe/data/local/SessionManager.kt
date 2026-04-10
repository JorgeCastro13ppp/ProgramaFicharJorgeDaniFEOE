    package com.example.programaficharfeoe.data.local

    import android.content.Context
    import android.content.SharedPreferences

    object SessionManager {

        private lateinit var prefs: SharedPreferences

        fun init(context: Context) {
            prefs = context.getSharedPreferences("app_session", Context.MODE_PRIVATE)
        }

        fun saveToken(token: String) {
            prefs.edit().putString("token", token).apply()
        }

        fun getToken(): String? {
            return prefs.getString("token", null)
        }

        fun saveUsername(username: String) {
            prefs.edit().putString("username", username).apply()
        }

        fun getUsername(): String? {
            return prefs.getString("username", null)
        }

        // 🔥 AÑADIR ESTO
        fun saveUserId(userId: Int) {
            prefs.edit().putInt("userId", userId).apply()
        }

        fun getUserId(): Int {
            return prefs.getInt("userId", 0)
        }

        fun clearSession() {
            prefs.edit().clear().apply()
        }
    }