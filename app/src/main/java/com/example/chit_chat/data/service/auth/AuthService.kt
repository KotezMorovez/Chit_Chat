package com.example.chit_chat.data.service.auth

import android.content.SharedPreferences
import android.util.Log
import com.example.chit_chat.R
import com.example.chit_chat.common.SharedPrefsService
import com.example.chit_chat.data.model.LoginRequestEntity
import com.example.chit_chat.data.model.UserTokenEntity
import retrofit2.awaitResponse
import javax.inject.Inject

interface AuthService {
    suspend fun login(request: LoginRequestEntity): Result<UserTokenEntity>
}

class AuthServiceImpl @Inject constructor(
    sharedPreferences: SharedPreferences
) : AuthService {
    private val api = AuthApi.getInstance(sharedPreferences)

    override suspend fun login(request: LoginRequestEntity): Result<UserTokenEntity> {
        try {
            val response = api.login(request).awaitResponse()
            if (!response.isSuccessful) {
                Log.e(R.string.app_name.toString(), response.code().toString())
                return Result.failure(IllegalStateException())
            }

            val tokens = response.body()

            return if (tokens != null) {
                Result.success(tokens)
            } else {
                Result.failure(IllegalStateException())
            }
        } catch (t: Throwable) {
            return Result.failure(t)
        }
    }
}
