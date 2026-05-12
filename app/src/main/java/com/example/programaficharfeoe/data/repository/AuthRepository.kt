package com.example.programaficharfeoe.data.repository

import com.example.programaficharfeoe.data.model.FcmTokenRequest
import com.example.programaficharfeoe.data.model.LoginRequest
import com.example.programaficharfeoe.data.model.LoginResponse
import com.example.programaficharfeoe.data.remote.ApiService
import com.example.programaficharfeoe.utils.ApiResult
import com.example.programaficharfeoe.utils.safeApiCall

class AuthRepository(private val api: ApiService) {

    suspend fun login(
        username: String,
        password: String
    ): ApiResult<LoginResponse> {

        return safeApiCall {

            api.login(
                LoginRequest(
                    username,
                    password
                )
            )
        }
    }

    suspend fun registrarTokenFCM(
        jwt: String,
        fcmToken: String
    ): ApiResult<Unit> {

        return safeApiCall {

            api.registrarTokenFCM(

                token = "Bearer $jwt",

                request = FcmTokenRequest(
                    token = fcmToken,
                    platform = "android"
                )
            )
        }
    }
}