package com.example.chit_chat.domain.interactor

import android.util.Log
import com.example.chit_chat.R
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
    suspend fun checkTokens(): Result<Unit>
}

class AuthInteractorImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : AuthInteractor {

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
            val updateProfileResult = profileRepository.updateProfileStorage()

            if (updateProfileResult.isSuccess) {
                Log.i(R.string.app_name.toString(), "Profile successfully updated")
            } else {
                Log.e(R.string.app_name.toString(), "Profile update failure")
            }

            Result.success(Unit)
        } else {
            Result.failure(IllegalStateException())
        }
    }

    override suspend fun checkTokens(): Result<Unit> {
        val result = authRepository.checkTokens()

        if (result.isFailure){
            return Result.failure(IllegalStateException())
        }

        val updateProfileResult = profileRepository.updateProfileStorage()

        if (updateProfileResult.isFailure) {
            return Result.failure(IllegalStateException())
        }

        return Result.success(Unit)
    }
}