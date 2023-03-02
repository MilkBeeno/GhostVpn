package com.simple.ghostvpn.network

import com.simple.ghostvpn.data.ApiResponse

suspend fun <T> retrofit(action: suspend () -> ApiResponse<T>): ApiResponse<T> {
    return try {
        val response = action()
        when (response.code) {
            2000 -> {
                response.successful = true
            }
            else -> response.successful = false
        }
        response
    } catch (e: Exception) {
        ApiResponse(-1, e.message.toString())
    }
}