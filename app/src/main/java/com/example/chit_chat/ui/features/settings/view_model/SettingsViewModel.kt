package com.example.chit_chat.ui.features.settings.view_model

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.R
import com.example.chit_chat.domain.profile.dto.Profile
import com.example.chit_chat.utils.BitmapUtils
import com.example.chit_chat.domain.profile.repository_api.ProfileRepository
import com.example.chit_chat.ui.features.settings.dto.SettingsProfileUI
import com.example.chit_chat.ui.features.settings.dto.toSettingsProfileUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor
) : ViewModel() {
    private var currentProfile: SettingsProfileUI? = null
    private var domainProfile: Profile? = null

    private val _profile = MutableSharedFlow<SettingsProfileUI>(1)
    val profile = _profile.asSharedFlow()

    private val _event = MutableSharedFlow<Int>(0)
    val event = _event.asSharedFlow()

    fun getProfile() {
        viewModelScope.launch {
            profileInteractor.getProfileSubscription().collect {
                domainProfile = it
                val profileUI = it.toSettingsProfileUI()
                currentProfile = profileUI
                _profile.emit(profileUI)
            }
        }
    }

    fun getImage(): String {
        return profileInteractor.getImageFromStorage()
    }

    fun uploadImage(uri: Uri, contentResolver: ContentResolver) {
        val profileUI = currentProfile
        if (profileUI != null) {

            viewModelScope.launch {
                val bitmap = BitmapUtils.getBitmapFromUri(uri, contentResolver)
                val storageUriResult =
                    profileInteractor.saveImage(
                        bitmap,
                        profileUI.id
                    )

                if (storageUriResult.isFailure) {
                    val exception = storageUriResult.exceptionOrNull()
                    if (exception != null) {
                        Log.e("Chit Chat Debug", exception.stackTraceToString())
                        _event.emit(R.string.settings_upload_image_error)
                    }
                } else {
                    saveImage(storageUriResult.getOrNull() ?: "")
                }
            }
        }
    }

    private suspend fun saveImage(imageURL: String) {
        val profileUI = currentProfile

        if (profileUI != null) {
            currentProfile = SettingsProfileUI(
                id = profileUI.id,
                avatar = imageURL
            )

            val profile = domainProfile?.copy(avatar = imageURL)

            if (profile != null) {
                val result = profileRepository.updateProfileData(profile)

                if (result.isFailure) {
                    val exception = result.exceptionOrNull()

                    if (exception != null) {
                        Log.e("Chit_Chat", exception.stackTraceToString())
                        _event.emit(R.string.settings_image_error)
                    }
            val result = profileInteractor.updateProfileData(profileUI.toDomain())
            if (result.isFailure) {
                val exception = result.exceptionOrNull()
                if (exception != null) {
                    Log.e("Chit_Chat", exception.stackTraceToString())
                    _event.emit(R.string.settings_image_error)
                }
            }
                }
            }
            profileInteractor.updateProfileStorage()
        }
    }
}
