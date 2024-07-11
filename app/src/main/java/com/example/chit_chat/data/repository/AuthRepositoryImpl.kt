package com.example.chit_chat.data.repository

import com.example.chit_chat.common.SharedPrefsService
import com.example.chit_chat.data.model.LoginRequestEntity
import com.example.chit_chat.data.model.SignUpRequestEntity
import com.example.chit_chat.data.service.auth.AuthService
import com.example.chit_chat.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val prefsService: SharedPrefsService
) : AuthRepository {


    override suspend fun login(email: String, password: String): Result<Unit> {
        val result = authService.login(LoginRequestEntity(email, password))
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
        email: String,
        password: String
    ): Result<Unit> {
        val result = authService.register(SignUpRequestEntity(firstName, lastName, email, password))
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
}