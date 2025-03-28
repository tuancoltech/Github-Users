package com.tymex.github.data.core.data.ext

import android.util.Log
import com.tymex.github.data.core.data.model.NetworkResponse
import retrofit2.Response

/**
 * Convert Retrofit Response to NetworkResponse
 */
fun <T> Response<T>.toNetworkResponse(): NetworkResponse<T> {
    this.run {
        if (!isSuccessful) {
            val message = errorBody()?.string()
            Log.e("toNetworkResponse", "response error: $message")
            return NetworkResponse.Error(message ?: "Unknown")
        } else {
            val body = body() ?: return NetworkResponse.Error(message = "Response body is empty")
            return NetworkResponse.Success(body)
        }
    }
}

/**
 * Convert Exception to NetworkResponse
 */
fun <T> Exception.toNetworkResponse(): NetworkResponse<T> {
    return NetworkResponse.Error(message = this.message.toString())
}

/**
 * Check if the 'rel="next"' is not present in the Link header, meaning it's the last page
 */
fun String.isLastPage(): Boolean {
    return !this.contains("rel=\"next\"")
}