package com.example.chit_chat.ui.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

interface LogoutHandler {
    suspend fun logout()
    val event: SharedFlow<Unit>
}

class LogoutHandlerImpl @Inject constructor() : LogoutHandler {
    private val _event = MutableSharedFlow<Unit>(0)
    override val event = _event.asSharedFlow()

    override suspend fun logout() {
        _event.emit(Unit)
    }
}