package com.example.chit_chat.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl
) : ViewModel() {
    private val _state = MutableSharedFlow<State>(0)
    val state = _state.asSharedFlow()

    fun login(email: String, password: String) {
        val validEmail = isValidEmail(email)
        val validPassword = isValidPassword(password)

        viewModelScope.launch {
            if (validEmail && validPassword) {
                val result = authRepository.login(email, password)
                if (result.isSuccess) {
                    _state.emit(State.NO_ERROR)
                } else {
                    _state.emit(State.INTERNET_ERROR)
                }
            } else if (!validEmail) {
                _state.emit(State.EMAIL_ERROR)
            } else {
                _state.emit(State.PASSWORD_ERROR)
            }

        }
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

    enum class State {
        NO_ERROR,
        EMAIL_ERROR,
        PASSWORD_ERROR,
        INTERNET_ERROR,
    }
}