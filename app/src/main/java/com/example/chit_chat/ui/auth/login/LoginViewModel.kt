package com.example.chit_chat.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.common.EMAIL_REGEX
import com.example.chit_chat.common.PASSWORD_REGEX
import com.example.chit_chat.domain.interactor.AuthInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : ViewModel() {
    private val _state = MutableSharedFlow<State>(1)
    val state = _state.asSharedFlow()
    private val _event = MutableSharedFlow<Event>(0)
    val event = _event.asSharedFlow()

    fun login(email: String, password: String) {
        val validEmail = isValidEmail(email)
        val validPassword = isValidPassword(password)

        viewModelScope.launch {
            if (validEmail && validPassword) {
                val result = authInteractor.login(email, password)
                if (result.isSuccess) {
                    _event.emit(Event.Success)
                } else {
                    _event.emit(Event.NetworkError)
                }
            } else {
                _state.emit(
                    State(
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

    sealed class Event {
        data object NetworkError : Event()
        data object Success : Event()
    }

    data class State(
        val isValidEmail: Boolean = true,
        val isValidPassword: Boolean = true
    )
}