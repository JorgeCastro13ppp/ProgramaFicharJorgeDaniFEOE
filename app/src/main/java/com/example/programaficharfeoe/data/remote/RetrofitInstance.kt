package com.example.programaficharfeoe.data.remote

import android.content.Context
import com.example.programaficharfeoe.R
import com.example.programaficharfeoe.data.local.SessionManager
import com.example.programaficharfeoe.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object RetrofitInstance {

    private const val TIMEOUT = 30L

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // SSL CONFIG (MKCERT)

    private fun getSecureClient(): OkHttpClient {

        val certificateFactory = CertificateFactory.getInstance("X.509")

        val inputStream: InputStream =
            appContext.resources.openRawResource(R.raw.mkcert_root)

        val ca = certificateFactory.generateCertificate(inputStream)

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(keyStore)

        val trustManagers = trustManagerFactory.trustManagers
        val trustManager = trustManagers[0] as X509TrustManager

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)

        // INTERCEPTORS

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")

            SessionManager.getToken()?.let { token ->
                requestBuilder.addHeader(
                    "Authorization",
                    "Bearer $token"
                )
            }

            chain.proceed(requestBuilder.build())
        }

        // OKHTTP CLIENT

        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)

            // 🔥 CLAVE: usar tu CA
            .sslSocketFactory(
                sslContext.socketFactory,
                trustManager
            )

            // ⚠️ SOLO DEV (porque usas IP)
            .hostnameVerifier { _, _ -> true }

            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // RETROFIT

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(getSecureClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}