package com.example.chit_chat.domain.profile.interactor

import com.example.chit_chat.domain.profile.dto.Chat
import com.example.chit_chat.domain.profile.dto.Profile
import com.example.chit_chat.domain.profile.repository_api.ProfileRepository
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
}