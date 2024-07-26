package com.example.chit_chat.data.service.profile

import com.example.chit_chat.data.mapper.toDomain
import com.example.chit_chat.data.model.ProfileEntity
import com.example.chit_chat.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

interface ProfileStorage {
    fun getProfile(): Profile?
    suspend fun setProfile(profile: ProfileEntity)
    suspend fun getProfileSubscription(): Flow<ProfileEntity>
}

class ProfileStorageImpl @Inject constructor(
) : ProfileStorage {
    private var profileEntity: ProfileEntity? = null
    private val _profileFlow = MutableSharedFlow<ProfileEntity>(1)

    override fun getProfile(): Profile? {
        return profileEntity?.toDomain()
    }

    override suspend fun setProfile(profile: ProfileEntity) {
        profileEntity = profile
        _profileFlow.emit(
            profile
        )
    }

    override suspend fun getProfileSubscription(): Flow<ProfileEntity> {
        return _profileFlow.asSharedFlow()
    }
}