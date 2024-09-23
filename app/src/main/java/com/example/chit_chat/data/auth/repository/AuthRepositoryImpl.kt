package com.example.chit_chat.data.auth.repository

import com.example.chit_chat.data.auth.dto.LoginRequestEntity
import com.example.chit_chat.data.auth.dto.SignUpRequestEntity
import com.example.chit_chat.data.auth.service.ApiService
import com.example.chit_chat.data.auth.service.SharedPrefsService
import com.example.chit_chat.domain.auth.repository_api.AuthRepository
import java.lang.IllegalStateException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val prefsService: SharedPrefsService
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        val result = apiService.login(LoginRequestEntity(email, password))
        return if (result.isSuccess) {
            val tokens = result.getOrNull()
            if (tokens != null) {
                prefsService.setAccessToken(tokens.accessToken)
                prefsService.setRefreshToken(tokens.refreshToken)
            }

            Result.success(Unit)
        } else {
            Result.failure(Throwable())
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        userName: String,
        email: String,
        password: String
    ): ResultWrapper<Unit> {
        val user = SignUpRequestEntity(firstName, lastName, userName, email, password)

        return apiService.register(user).mapSuccess {
            prefsService.setAccessToken(it.accessToken)
            prefsService.setRefreshToken(it.refreshToken)
        }
    }

    override fun checkTokens(): Result<Unit> {
        val resultAccessToken = prefsService.getAccessToken().isNotEmpty()
        val resultRefreshToken = prefsService.getRefreshToken().isNotEmpty()

        return if (resultAccessToken && resultRefreshToken) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalStateException())
        }
    }
}