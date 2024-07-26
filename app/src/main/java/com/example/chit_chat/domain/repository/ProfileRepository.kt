package com.example.chit_chat.domain.repository

import android.graphics.Bitmap
import com.example.chit_chat.domain.model.Profile
import com.example.chit_chat.ui.model.ProfileUI
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun createProfile(firstName: String, lastName: String): Result<Unit>
    suspend fun updateProfileStorage(): Result<Unit>
    suspend fun getProfileSubscription(): Flow<Profile>
    suspend fun saveImage(image: Bitmap, id: String): Result<String>
    suspend fun updateProfileData(profile: Profile): Result<Unit>
    suspend fun setProfileToStorage(profileUI: ProfileUI)
    fun getProfileFromStorage(): Profile
    fun getImageFromStorage(): String
}