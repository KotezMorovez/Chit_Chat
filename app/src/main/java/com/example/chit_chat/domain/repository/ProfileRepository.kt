package com.example.chit_chat.domain.repository

import android.graphics.Bitmap
import com.example.chit_chat.domain.model.Chat
import com.example.chit_chat.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun createProfile(firstName: String, lastName: String): Result<Unit>
    suspend fun updateProfileStorage(): Result<Unit>
    suspend fun getProfileSubscription(): Flow<Profile>
    suspend fun getChatListSubscription(id: String): Flow<List<Chat>>
    suspend fun deleteChat(userId: String, chatId: String): Result<Unit>
    suspend fun saveImage(image: Bitmap, id: String): Result<String>
    suspend fun updateProfileData(profile: Profile): Result<Unit>
    suspend fun setProfileToStorage(profile: Profile)
    fun getProfileFromStorage(): Profile?
    fun getImageFromStorage(): String
}