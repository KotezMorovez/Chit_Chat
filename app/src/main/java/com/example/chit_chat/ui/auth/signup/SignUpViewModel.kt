package com.example.chit_chat.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.common.EMAIL_REGEX
import com.example.chit_chat.common.PASSWORD_REGEX
import com.example.chit_chat.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableSharedFlow<State>(0)
    val state = _state.asSharedFlow()

    fun signUp(firstName: String, lastName: String, email: String, password: String) {
        val validEmail = isValidEmail(email)
        val validPassword = isValidPassword(password)

        viewModelScope.launch {
            if (firstName.isNotEmpty() && lastName.isNotEmpty() && validEmail && validPassword) {
                val result = authRepository.register(firstName, lastName, email, password)
                if (result.isSuccess) {
                    _state.emit(State.Success)
                } else {
                    _state.emit(State.InternetError)
                }
            } else {
                _state.emit(
                    State.Error(
                        firstName.isEmpty(),
                        lastName.isEmpty(),
                        !validEmail,
                        !validPassword
                    )
                )
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches()
    }

    private fun isValidEmail(email: String): Boolean {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches()
    }

    sealed class State {
        data object InternetError : State()
        data class Error(
            val isValidFirstName: Boolean = true,
            val isValidLastName: Boolean = true,
            val isValidEmail: Boolean = true,
            val isValidPassword: Boolean = true
        ) : State()

        data object Success : State()
    }
}