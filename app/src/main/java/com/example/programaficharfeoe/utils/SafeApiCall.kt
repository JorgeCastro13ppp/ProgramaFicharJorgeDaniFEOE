package com.example.programaficharfeoe.utils

import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): ApiResult<T> {

    return try {

        ApiResult.Success(apiCall())

    } catch (e: HttpException) {

        ApiResult.Error(
            "Error del servidor (${e.code()})"
        )

    } catch (e: IOException) {

        ApiResult.Error(
            "Error de conexión"
        )

    } catch (e: Exception) {

        ApiResult.Error(
            e.message ?: "Error desconocido"
        )
    }
}