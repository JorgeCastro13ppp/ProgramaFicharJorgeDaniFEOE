package com.example.programaficharfeoe.utils

sealed class ApiResult<out T> {

    data class Success<T>(
        val data: T
    ) : ApiResult<T>()

    data class Error(
        val message: String
    ) : ApiResult<Nothing>()
}