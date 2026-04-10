package com.example.programaficharfeoe.data.remote

import android.util.Log
import com.example.programaficharfeoe.data.local.SessionManager
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.1.189:8080/"

    // 🔍 Interceptor para logs
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 🔐 Interceptor para añadir el token JWT
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = SessionManager.getToken()

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            Log.d("AUTH", "Token enviado correctamente")
        } else {
            Log.e("AUTH", "Token nulo o vacío")
        }

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val api: FichajeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FichajeApiService::class.java)
    }
}