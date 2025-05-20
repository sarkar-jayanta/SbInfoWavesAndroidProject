package com.example.mytestproject.utils

import android.content.Context
import android.util.Log
import com.example.mytestproject.MyTestApplication
import com.sit.common.utils.NoInternetException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

class SafeApiRequest(private val context: Context) {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        try {
            val response = call.invoke()

            if (response.isSuccessful) {
                return response.body() ?: throw ApiException("Response body is null")
            } else {
                val errorMessage = parseErrorMessage(response)

                when (response.code()) {
                    401, 403 -> {
                        Log.d("Exception", "Unauthorized or session expired")
                        if (context is MyTestApplication) {

                        }
                        throw AuthException("Session expired or unauthorized")
                    }

                    else -> {
                        Log.d("Exception", errorMessage)
                        throw ApiException(errorMessage)
                    }
                }
            }

        } catch (ex: NoInternetException) {
            Log.e("SafeApiRequest", "No internet connection", ex)
            throw ApiException("No internet connection")
        } catch (ex: IOException) {
            Log.e("SafeApiRequest", "Network error", ex)
            throw ApiException("Network error: ${ex.localizedMessage}")
        } catch (ex: Exception) {
            Log.e("SafeApiRequest", "Unexpected error", ex)
            throw ApiException("Unexpected error: ${ex.localizedMessage}")
        }
    }

    private fun parseErrorMessage(response: Response<*>): String {
        return try {
            val error = response.errorBody()?.string()
            val json = JSONObject(error ?: "")
            json.optString("message", json.optString("Message", "Something went wrong. Please try again!"))
        } catch (e: Exception) {
            "Something went wrong. Please try again!"
        }
    }
}
