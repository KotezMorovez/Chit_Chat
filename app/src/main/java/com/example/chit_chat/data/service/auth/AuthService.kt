package com.example.chit_chat.data.service.auth

import android.util.Log
import com.example.chit_chat.R
import retrofit2.awaitResponse
import javax.inject.Inject

interface ApiService {

}

class ApiServiceImpl @Inject constructor() : ApiService {
    private val api = AuthApi.getInstance()

    suspend fun testRequest(): Result<Any> {
        try {
            val response = api.test().awaitResponse()

            if (!response.isSuccessful) {
                Log.e(R.string.app_name.toString(), response.code().toString())
                return Result.failure(IllegalStateException())
            }

            val obj = response.body()

            return if (obj != null) {
                Result.success(obj)
            } else {
                Result.failure(IllegalStateException())
            }
        } catch (t: Throwable) {
            return Result.failure(t)
        }
    }
}
