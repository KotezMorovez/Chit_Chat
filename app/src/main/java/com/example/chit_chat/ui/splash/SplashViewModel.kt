package com.example.chit_chat.ui.splash

import androidx.lifecycle.ViewModel
import com.example.chit_chat.domain.repository.AuthRepository
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun checkToken(): Boolean {
        return authRepository.checkToken()
    }
}