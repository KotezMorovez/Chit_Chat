package com.example.chit_chat.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
}