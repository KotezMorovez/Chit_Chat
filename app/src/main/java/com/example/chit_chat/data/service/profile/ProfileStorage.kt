package com.example.chit_chat.data.service.profile

import com.example.chit_chat.data.model.ProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

interface ProfileStorage {
    fun getProfile(): ProfileEntity?
    suspend fun setProfile(profile: ProfileEntity)
    fun getProfileSubscription(): Flow<ProfileEntity>
}

class ProfileStorageImpl @Inject constructor(
) : ProfileStorage {
    private var profileEntity: ProfileEntity? = null
    private val _profileFlow = MutableSharedFlow<ProfileEntity>(1)

    override fun getProfile(): ProfileEntity? {
        return profileEntity
    }

    override suspend fun setProfile(profile: ProfileEntity) {
        profileEntity = profile
        _profileFlow.emit(
            profile
        )
    }

    override fun getProfileSubscription(): Flow<ProfileEntity> {
        return _profileFlow.asSharedFlow()
    }
}