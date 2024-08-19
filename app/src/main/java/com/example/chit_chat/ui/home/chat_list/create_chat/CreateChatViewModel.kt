package com.example.chit_chat.ui.home.chat_list.create_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.domain.interactor.ProfileInteractor
import com.example.chit_chat.ui.mapper.toUI
import com.example.chit_chat.ui.model.ProfileUI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateChatViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor
) : ViewModel() {
    private val _profile = MutableSharedFlow<ProfileUI>(1)
    val profile = _profile.asSharedFlow()

    fun subscribeProfileUpdate() {
        viewModelScope.launch {
            val flow = profileInteractor.getProfileSubscription()

            flow.collect {
                _profile.emit(it.toUI())
            }
        }
    }
}