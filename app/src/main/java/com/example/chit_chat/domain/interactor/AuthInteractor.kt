package com.example.chit_chat.domain.interactor

import com.example.chit_chat.domain.repository.AuthRepository
import com.example.chit_chat.domain.repository.ProfileRepository
import java.lang.IllegalStateException
import javax.inject.Inject

interface AuthInteractor {
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<Unit>

    suspend fun login(email: String, password: String): Result<Unit>
}

class AuthInteractorImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
): AuthInteractor {

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<Unit> {
        val result = authRepository.register(firstName, lastName, email, password)

        return if (result.isSuccess) {
            profileRepository.createProfile(firstName, lastName)
        } else {
            Result.failure(IllegalStateException())
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        val result = authRepository.login(email, password)
         return if (result.isSuccess) {
             profileRepository.updateProfileStorage()
             Result.success(Unit)
        } else {
            Result.failure(IllegalStateException())
        }
    }
}