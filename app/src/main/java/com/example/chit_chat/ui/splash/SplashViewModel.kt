package com.example.chit_chat.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.domain.interactor.AuthInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : ViewModel() {
    private val _event = MutableSharedFlow<Event>(0)
    val event = _event.asSharedFlow()

    fun checkTokenExist() {
        viewModelScope.launch {
            val result = authInteractor.checkTokens()

            if (result.isSuccess) {
                _event.emit(Event.SUCCESS)
            } else {
                _event.emit(Event.FAILURE)
            }
        }
    }

    enum class Event {
        SUCCESS,
        FAILURE
    }
}