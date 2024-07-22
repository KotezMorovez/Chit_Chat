package com.example.chit_chat.domain.repository

import com.example.chit_chat.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun createProfile(firstName: String, lastName: String): Result<Unit>
    suspend fun updateProfileStorage(): Result<Unit>
    suspend fun getProfileSubscription(): Flow<Profile>
}