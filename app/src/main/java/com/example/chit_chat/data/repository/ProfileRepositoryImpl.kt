package com.example.chit_chat.data.repository

import android.util.Log
import com.example.chit_chat.R
import com.example.chit_chat.data.mapper.toDomain
import com.example.chit_chat.data.model.ProfileEntity
import com.example.chit_chat.data.service.FirebaseService
import com.example.chit_chat.data.service.ProfileStorage
import com.example.chit_chat.data.service.auth.ApiService
import com.example.chit_chat.domain.model.Profile
import com.example.chit_chat.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.IllegalStateException
import java.lang.NullPointerException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val firebaseService: FirebaseService,
    private val profileStorage: ProfileStorage
) : ProfileRepository {
    override suspend fun createProfile(firstName: String, lastName: String): Result<Unit> {
        val profile = getProfileFromAuthApi().getOrNull()
        if (profile != null) {
            val firebaseResult = firebaseService.register(profile, profile.id)

            if (firebaseResult.isFailure) {
                val error = firebaseResult.exceptionOrNull()
                if (error != null) {
                    Log.e(R.string.app_name.toString(), error.toString())
                    return Result.failure(error)
                }
            }

            profileStorage.setProfile(profile)
            return Result.success(Unit)
        }
        return Result.failure(NullPointerException())
    }

    override suspend fun updateProfileStorage(): Result<Unit> {
        val id = getProfileFromAuthApi().getOrNull()?.id

        if (id != null) {
            val profileResult = firebaseService.getProfileById(id)

            if (profileResult.isFailure) {
                val error = profileResult.exceptionOrNull()
                if (error != null) {
                    Log.e(R.string.app_name.toString(), error.toString())
                    return Result.failure(error)
                }
            }

            val profile = profileResult.getOrNull()
            if (profile != null) {
                profileStorage.setProfile(profile)
                Result.success(Unit)
            }
        }

        return Result.failure(IllegalStateException())
    }

    override suspend fun getProfileSubscription(): Flow<Profile> {
        return profileStorage.getProfileSubscription().map { it.toDomain() }
    }

    private suspend fun getProfileFromAuthApi(): Result<ProfileEntity?> {
        val profileResult = apiService.getProfile()

        if (profileResult.isFailure) {
            val error = profileResult.exceptionOrNull()
            if (error != null) {
                Log.e(R.string.app_name.toString(), error.toString())
                return Result.failure(error)
            }
        }

        return Result.success(profileResult.getOrNull())
    }
}