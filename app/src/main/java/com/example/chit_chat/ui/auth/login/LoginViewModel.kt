package com.example.chit_chat.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.common.EMAIL_REGEX
import com.example.chit_chat.common.PASSWORD_REGEX
import com.example.chit_chat.domain.repository.AuthRepository
import com.example.chit_chat.ui.auth.signup.SignUpViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _event = MutableSharedFlow<Event>(1)
    val event = _event.asSharedFlow()
    private val _state = MutableSharedFlow<State>(0)
    val state = _state.asSharedFlow()

    fun login(email: String, password: String) {
        val validEmail = isValidEmail(email)
        val validPassword = isValidPassword(password)

        viewModelScope.launch {
            if (validEmail && validPassword) {
                val result = authRepository.login(email, password)
                if (result.isSuccess) {
                    _state.emit(State.Success)
                } else {
                    _state.emit(State.NetworkError)
                }
            } else {
                _event.emit(
                    Event(
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
        data object NetworkError : State()
        data object Success : State()
    }

    data class Event(
        val isValidEmail: Boolean = true,
        val isValidPassword: Boolean = true
    )
}