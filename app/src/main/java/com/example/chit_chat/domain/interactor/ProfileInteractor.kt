package com.example.chit_chat.domain.interactor

import android.graphics.Bitmap
import com.example.chit_chat.domain.model.Chat
import com.example.chit_chat.domain.model.Profile
import com.example.chit_chat.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException
import javax.inject.Inject

interface ProfileInteractor {
    suspend fun getProfileSubscription(): Flow<Profile>
    suspend fun leaveChat(
        userId: String,
        chatId: String,
        userIdList: ArrayList<String>
    ): Result<Unit>
    suspend fun getChatListSubscription(id: String): Flow<List<Chat>>
    fun getCurrentProfileId(): String?
    fun getImageFromStorage(): String
    suspend fun saveImage(bitmap: Bitmap, id: String): Result<String>
    suspend fun getProfileContactsList(): List<String>
    suspend fun updateProfileData(profile: Profile): Result<Unit>
    suspend fun updateProfileStorage(): Result<Unit>
}

class ProfileInteractorImpl @Inject constructor(
    private val profileRepository: ProfileRepository
) : ProfileInteractor {

    override suspend fun getProfileSubscription(): Flow<Profile> {
        return profileRepository.getProfileSubscription()
    }

    override suspend fun leaveChat(
        userId: String,
        chatId: String,
        userIdList: ArrayList<String>
    ): Result<Unit> {
        if (userIdList.size > 1) {
            val updatedIdList = ArrayList(userIdList.filter {
                it != userId
            })
            val result = profileRepository.updateChatParticipants(updatedIdList, chatId)

            if (result.isFailure) {
                return Result.failure(IllegalStateException())
            }
        } else {
            val globalResult = profileRepository.deleteChat(chatId)
            if (globalResult.isFailure) {
                return Result.failure(IllegalStateException())
            }
        }

        return Result.success(Unit)
    }

    override suspend fun getChatListSubscription(id: String): Flow<List<Chat>> {
        return profileRepository.getChatListSubscription(id)
    }

    override fun getCurrentProfileId(): String? {
        return profileRepository.getProfileFromStorage()?.id
    }

    override fun getImageFromStorage(): String {
        return profileRepository.getProfileFromStorage()?.avatar ?: ""
    }

    override suspend fun saveImage(bitmap: Bitmap, id: String): Result<String> {
        return profileRepository.saveImage(bitmap, id)
    }

    override suspend fun getProfileContactsList(): List<String> {
        return profileRepository.getProfileFromStorage()?.contacts ?: listOf()
    }

    override suspend fun updateProfileData(profile: Profile): Result<Unit> {
        return profileRepository.updateProfileData(profile)
    }

    override suspend fun updateProfileStorage(): Result<Unit> {
        return profileRepository.updateProfileStorage()
    }
}