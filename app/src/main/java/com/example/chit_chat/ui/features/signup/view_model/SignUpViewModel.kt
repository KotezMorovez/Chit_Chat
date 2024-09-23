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

    fun signUp(
        firstName: String,
        lastName: String,
        userName: String,
        email: String,
        password: String
    ) {
        val validEmail = isValidEmail(email)
        val validPassword = isValidPassword(password)
        val validFirstName = firstName.isNotEmpty()
        val validLastName = lastName.isNotEmpty()
        var validUserName = isValidUserName(userName)
        var isUniqueUserName = true
        var isUniqueEmail = true

        viewModelScope.launch {
            if (validFirstName && validLastName && validUserName && validEmail && validPassword) {
                val result = authInteractor.register(firstName, lastName, userName, email, password)

                if (result.isSuccessful()) {
                    _event.emit(Event.Success)
                    return@launch
                }

                when (val error = result.wrappedError) {
                    is Error.NetworkError -> {
                        _event.emit(Event.NetworkError)
                        return@launch
                    }

                    is Error.BackendError -> {
                        when (error.error.code) {
                            0 -> {
                                _event.emit(Event.UnknownError)
                                return@launch
                            }

                            1 -> {
                                isUniqueEmail = false
                            }

                            2 -> {
                                isUniqueUserName = false
                            }

                            3 -> {
                                validUserName = false
                            }
                        }
                    }

                    else -> {
                        _event.emit(Event.UnknownError)
                        return@launch
                    }
                }
            }

            _state.emit(
                State(
                    isValidFirstName = !validFirstName,
                    isValidLastName = !validLastName,
                    isValidPassword = !validPassword,
                    isValidEmail = if (validEmail && isUniqueEmail) {
                        null
                    } else if (!validEmail) {
                        R.string.auth_email_error_hint
                    } else {
                        R.string.sign_up_email_already_exist
                    },
                    isValidUserName = if (validUserName && isUniqueUserName) {
                        null
                    } else if (!validUserName) {
                        R.string.sign_up_user_name_validate_fail
                    } else {
                        R.string.sign_up_user_name_already_exist
                    }
                )
            )
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches()
    }

    private fun isValidEmail(email: String): Boolean {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches()
    }

    private fun isValidUserName(userName: String): Boolean {
        return Pattern.compile(USERNAME_REGEX).matcher(userName).matches()
    }

    sealed class Event {
        data object NetworkError : Event()
        data object UnknownError : Event()
        data object Success : Event()
    }

    data class State(
        val isValidFirstName: Boolean = true,
        val isValidLastName: Boolean = true,
        val isValidEmail: Int? = null,
        val isValidPassword: Boolean = true,
        val isValidUserName: Int? = null
    )
}