package com.example.chit_chat.ui.login

import androidx.lifecycle.ViewModel
import com.example.chit_chat.data.repository.AuthRepositoryImpl
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl
) : ViewModel() {


    fun login(email: String, password: String) {

    }
}