package com.example.chit_chat.domain.auth.interactor

import android.util.Log
import com.example.chit_chat.R
import com.example.chit_chat.domain.auth.repository_api.AuthRepository
import com.example.chit_chat.domain.profile.repository_api.ProfileRepository
import com.example.chit_chat.utils.ResultWrapper
import java.lang.IllegalStateException
import javax.inject.Inject

interface AuthInteractor {
    suspend fun register(
        firstName: String,
        lastName: String,
        userName: String,
        email: String,
        password: String
    ): ResultWrapper<Unit>

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
        userName: String,
        email: String,
        password: String
    ): ResultWrapper<Unit> {
        val result = authRepository.register(firstName, lastName, userName, email, password)

        return when(result){
            is ResultWrapper.Success -> {
                val createProfileResult = profileRepository.createProfile(firstName, lastName)
                if (createProfileResult.isSuccess) {
                    ResultWrapper.Success(Unit)
                } else { result }
            }
            else -> {
                result
            }
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