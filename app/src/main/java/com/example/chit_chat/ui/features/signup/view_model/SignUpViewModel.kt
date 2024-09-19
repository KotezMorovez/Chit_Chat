package com.example.chit_chat.ui.features.signup.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.utils.EMAIL_REGEX
import com.example.chit_chat.utils.PASSWORD_REGEX
import com.example.chit_chat.domain.auth.interactor.AuthInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : ViewModel() {
    private val _state = MutableSharedFlow<State>(1)
    val state = _state.asSharedFlow()
    private val _event = MutableSharedFlow<Event>(0)
    val event = _event.asSharedFlow()

    fun signUp(firstName: String, lastName: String, email: String, password: String) {
        val validEmail = isValidEmail(email)
        val validPassword = isValidPassword(password)
        val validFirstName = firstName.isNotEmpty()
        val validLastName = lastName.isNotEmpty()

        viewModelScope.launch {
            if (validFirstName && validLastName && validEmail && validPassword) {
                val result = authInteractor.register(firstName, lastName, email, password)
                if (result.isSuccess) {
                    _event.emit(Event.Success)
                } else {
                    _event.emit(Event.NetworkError)
                }
            } else {
                _state.emit(
                    State(
                        !validFirstName,
                        !validLastName,
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
        val isValidFirstName: Boolean = true,
        val isValidLastName: Boolean = true,
        val isValidEmail: Boolean = true,
        val isValidPassword: Boolean = true
    )
}