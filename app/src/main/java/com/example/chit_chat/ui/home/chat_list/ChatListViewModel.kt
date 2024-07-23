package com.example.chit_chat.ui.home.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.domain.mapper.toUI
import com.example.chit_chat.domain.repository.ProfileRepository
import com.example.chit_chat.ui.model.ProfileUI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _profile = MutableSharedFlow<ProfileUI>(1)
    val profile = _profile.asSharedFlow()

    fun getProfile() {
        viewModelScope.launch {
            profileRepository.getProfileSubscription().collect {
                val profileUI = it.toUI()
                _profile.emit(profileUI)
            }
        }
    }
}