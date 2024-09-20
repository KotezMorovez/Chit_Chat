package com.example.chit_chat.data.auth.service


import android.util.Log
import com.example.chit_chat.R
import com.example.chit_chat.data.auth.dto.LoginRequestEntity
import com.example.chit_chat.data.auth.dto.SignUpRequestEntity
import com.example.chit_chat.data.auth.dto.UserTokenEntity
import com.example.chit_chat.data.profile.dto.profile.ProfileEntity
import retrofit2.awaitResponse
import javax.inject.Inject

interface ApiService {
    suspend fun login(request: LoginRequestEntity): Result<UserTokenEntity>
    suspend fun register(request: SignUpRequestEntity): Result<UserTokenEntity>
    suspend fun getProfile(): Result<ProfileEntity>
    suspend fun refreshToken(request: UserTokenEntity): Result<UserTokenEntity>
}

class ApiServiceImpl @Inject constructor(
    private val authApi: AuthApi
) : ApiService {

    override suspend fun login(request: LoginRequestEntity): Result<UserTokenEntity> {
        try {
            val response = authApi.login(request).awaitResponse()
            if (!response.isSuccessful) {
                Log.e(TAG, response.code().toString())
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

    override suspend fun register(request: SignUpRequestEntity): Result<UserTokenEntity> {
        try {
            val response = authApi.register(request).awaitResponse()
            if (!response.isSuccessful) {
                Log.e(TAG, response.code().toString())
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

    override suspend fun getProfile(): Result<ProfileEntity> {
        try {
            val response = authApi.getProfile().awaitResponse()
            if (!response.isSuccessful) {
                Log.e(TAG, response.code().toString())
                return Result.failure(IllegalStateException())
            }

            val profile = response.body()

            return if (profile != null) {
                Result.success(profile)
            } else {
                Result.failure(IllegalStateException())
            }
        } catch (t: Throwable) {
            return Result.failure(t)
        }
    }

    override suspend fun refreshToken(request: UserTokenEntity): Result<UserTokenEntity> {
        try {
            val response = authApi.refreshToken(request).awaitResponse()

            if (!response.isSuccessful) {
                Log.e(TAG, response.code().toString())
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

    companion object{
        private const val TAG = "Chit Chat Debug"
    }
}
