package com.example.chit_chat.ui.home.settings

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.R
import com.example.chit_chat.common.BitmapUtils
import com.example.chit_chat.domain.mapper.toDomain
import com.example.chit_chat.domain.mapper.toUI
import com.example.chit_chat.domain.repository.ProfileRepository
import com.example.chit_chat.ui.model.ProfileUI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private var currentProfile: ProfileUI? = null

    private val _profile = MutableSharedFlow<ProfileUI>(1)
    val profile = _profile.asSharedFlow()

    private val _event = MutableSharedFlow<Int>(0)
    val event = _event.asSharedFlow()

    fun getProfile() {
        viewModelScope.launch {
            profileRepository.getProfileSubscription().collect {
                val profileUI = it.toUI()
                currentProfile = profileUI
                _profile.emit(profileUI)
            }
        }
    }

    fun getImage(): String {
        return profileRepository.getImageFromStorage()
    }

    fun uploadImage(uri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            val bitmap = BitmapUtils.getBitmapFromUri(uri, contentResolver)
            val storageUriResult =
                profileRepository.saveImage(bitmap, profileRepository.getProfileFromStorage()!!.id)

            if (storageUriResult.isFailure) {
                val exception = storageUriResult.exceptionOrNull()
                if (exception != null) {
                    Log.e(R.string.app_name.toString(), exception.stackTraceToString())
                    _event.emit(R.string.settings_upload_image_error)
                }
            } else {
                saveImage(storageUriResult.getOrNull() ?: "")
            }
        }
    }

    private suspend fun saveImage(imageURL: String) {
        val profileUI = currentProfile
        if (profileUI != null) {
            currentProfile = ProfileUI(
                id = profileUI.id,
                email = profileUI.email,
                avatar = imageURL,
                firstName = profileUI.firstName,
                lastName = profileUI.lastName,
            )

            viewModelScope.launch {
                profileRepository.setProfileToStorage(currentProfile!!.toDomain())
            }

            val result = profileRepository.updateProfileData(currentProfile!!.toDomain())
            if (result.isFailure) {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    Log.e(R.string.app_name.toString(), exception.stackTraceToString())
                    _event.emit(R.string.settings_image_error)
                }
            }
            profileRepository.updateProfileStorage()
        }
    }
}
