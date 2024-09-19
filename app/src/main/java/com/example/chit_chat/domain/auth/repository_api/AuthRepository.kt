package com.example.chit_chat.domain.auth.repository_api

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<Unit>
    fun checkTokens(): Result<Unit>
}