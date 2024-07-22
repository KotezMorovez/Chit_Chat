package com.example.chit_chat.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.domain.repository.AuthRepository
import com.example.chit_chat.domain.repository.ProfileRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    fun updateProfile() {
        viewModelScope.launch {
            profileRepository.updateProfileStorage()
        }
    }

    fun checkToken(): Boolean {
        return authRepository.checkToken()
    }
}