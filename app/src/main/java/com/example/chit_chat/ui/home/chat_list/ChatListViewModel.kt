package com.example.chit_chat.ui.home.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.domain.model.Profile
import com.example.chit_chat.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _profile = MutableSharedFlow<Profile>(1)
    val profile = _profile.asSharedFlow()

    fun getProfile() {
        viewModelScope.launch {
            profileRepository.getProfileSubscription().collect {
                _profile.emit(it)
            }
        }
    }
}