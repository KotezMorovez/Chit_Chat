package com.example.chit_chat.domain.repository

import com.example.chit_chat.domain.model.Profile

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<Unit>
    fun checkToken(): Boolean
}