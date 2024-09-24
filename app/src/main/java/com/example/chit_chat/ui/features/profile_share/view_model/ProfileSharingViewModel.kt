package com.example.chit_chat.ui.features.profile_share.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.domain.profile.interactor.ProfileInteractor
import com.example.chit_chat.ui.features.profile_share.dto.SharingProfileUI
import com.example.chit_chat.ui.features.profile_share.dto.toSharingProfileUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSharingViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor
) : ViewModel() {
    private val _sharingProfile = MutableStateFlow(SharingProfileUI("", ""))
    val sharingProfile = _sharingProfile.asStateFlow()

    fun getProfileInfo() {
        viewModelScope.launch {
            val profileFlow = profileInteractor.getProfileSubscription()
            profileFlow.collect{
                _sharingProfile.emit(it.toSharingProfileUI())
            }
        }
    }
}