package com.example.chit_chat.data.repository

import android.graphics.Bitmap
import android.util.Log
import com.example.chit_chat.R
import com.example.chit_chat.utils.BitmapUtils
import com.example.chit_chat.data.mapper.toDomain
import com.example.chit_chat.data.mapper.toProfileEntity
import com.example.chit_chat.data.model.ProfileEntity
import com.example.chit_chat.data.service.profile.FirebaseService
import com.example.chit_chat.data.service.profile.ProfileStorage
import com.example.chit_chat.data.service.auth.ApiService
import com.example.chit_chat.data.service.profile.CloudStorageService
import com.example.chit_chat.domain.model.Chat
import com.example.chit_chat.domain.model.Contact
import com.example.chit_chat.domain.model.Profile
import com.example.chit_chat.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.IllegalStateException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val firebaseService: FirebaseService,
    private val profileStorage: ProfileStorage,
    private val cloudStorageService: CloudStorageService
) : ProfileRepository {
    override suspend fun createProfile(firstName: String, lastName: String): Result<Unit> {
        val profile = getProfileFromAuthApi().getOrNull()
        if (profile != null) {
            val firebaseResult = firebaseService.saveProfile(profile)

            if (firebaseResult.isFailure) {
                val error = firebaseResult.exceptionOrNull()
                return if (error != null) {
                    Log.e(R.string.app_name.toString(), error.toString())
                    Result.failure(error)
                } else {
                    Result.failure(IllegalStateException())
                }
            }

            profileStorage.setProfile(profile)
            return Result.success(Unit)
        }
        return Result.failure(IllegalStateException())
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
                return Result.success(Unit)
            }
        }

        return Result.failure(IllegalStateException())
    }

    override suspend fun getProfileSubscription(): Flow<Profile> {
        return profileStorage.getProfileSubscription().map { it.toDomain() }
    }

    override fun getProfileFromStorage(): Profile? {
        return profileStorage.getProfile()?.toDomain()
    }

    override suspend fun setProfileToStorage(profile: Profile) {
        return profileStorage.setProfile(profile.toProfileEntity())
    }

    override suspend fun saveImage(image: Bitmap, id: String): Result<String> {
        val byteArray = BitmapUtils.convertBitmapToByteArray(image)
        return cloudStorageService.uploadImage(byteArray, id)
    }

    override suspend fun updateProfileData(profile: Profile): Result<Unit> {
        return firebaseService.saveProfile(profile.toProfileEntity())
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

    override suspend fun deleteChat(chatId: String): Result<Unit> {
        return firebaseService.deleteChatGlobally(chatId)
    }

    override suspend fun updateChatParticipants(
        userIdList: ArrayList<String>,
        chatId: String
    ): Result<Unit> {
        return firebaseService.updateChatParticipants(userIdList, chatId)
    }

    override suspend fun getChatListSubscription(id: String): Flow<List<Chat>> {
        return firebaseService.observeChatList(id).map { list ->
            list.map {
                it.toDomain()
            }
        }
    }

    override suspend fun getContactsFromList(contactsProfileList: List<String>): Result<List<Contact>> {
        return firebaseService.getContactsFromList(contactsProfileList).map { list ->
            list.map {
                it.toDomain()
            }
        }
    }
}