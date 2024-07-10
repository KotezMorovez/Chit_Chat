package com.example.chit_chat.ui.auth.signup

import androidx.lifecycle.ViewModel
import com.example.chit_chat.data.repository.AuthRepositoryImpl
import com.example.chit_chat.ui.auth.login.LoginViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.regex.Pattern
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl
) : ViewModel() {
    private val _event = MutableSharedFlow<LoginViewModel.State>(0)
    val event = _event.asSharedFlow()

    fun signUp(){

    }

    private fun isValidPassword(password: String): Boolean {
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches()
    }

    private fun isValidEmail(email: String): Boolean {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches()
    }

    companion object {
        private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-.]+\\.[A-Za-z0-9]{2,}\$"
        private const val PASSWORD_REGEX =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$"
    }
}