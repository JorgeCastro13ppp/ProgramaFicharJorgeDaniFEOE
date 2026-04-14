package com.example.programaficharfeoe.data.remote

import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val baseUrl = Constants.BASE_URL

    private val authInterceptor = Interceptor { chain ->

        val originalRequest: Request = chain.request()

        val token = SessionManager.getToken()

        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}