package com.example.chit_chat.domain.profile.repository_api

import android.graphics.Bitmap
import com.example.chit_chat.domain.profile.dto.Chat
import com.example.chit_chat.domain.profile.dto.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun createProfile(firstName: String, lastName: String): Result<Unit>
    suspend fun updateProfileStorage(): Result<Unit>
    suspend fun getProfileSubscription(): Flow<Profile>
    suspend fun getChatListSubscription(id: String): Flow<List<Chat>>
    suspend fun deleteChat(chatId: String): Result<Unit>
    suspend fun updateChatParticipants(
        userIdList: ArrayList<String>,
        chatId: String
    ): Result<Unit>
    suspend fun saveImage(image: Bitmap, id: String): Result<String>
    suspend fun updateProfileData(profile: Profile): Result<Unit>
    suspend fun setProfileToStorage(profile: Profile)
    fun getProfileFromStorage(): Profile?
    fun getImageFromStorage(): String
}