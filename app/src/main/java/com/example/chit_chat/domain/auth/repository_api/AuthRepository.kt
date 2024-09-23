package com.example.chit_chat.domain.auth.repository_api

import com.example.chit_chat.utils.ResultWrapper

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(
        firstName: String,
        lastName: String,
        userName: String,
        email: String,
        password: String
    ): ResultWrapper<Unit>
    fun checkTokens(): Result<Unit>
}