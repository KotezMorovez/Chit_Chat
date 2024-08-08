package com.example.chit_chat.ui.home.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.R
import com.example.chit_chat.domain.mapper.toUI
import com.example.chit_chat.domain.repository.ProfileRepository
import com.example.chit_chat.ui.common.DateUtils
import com.example.chit_chat.ui.home.chat_list.adapter.ChatItem
import com.example.chit_chat.ui.model.ProfileUI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val dateUtils: DateUtils,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _profile = MutableSharedFlow<ProfileUI>(1)
    val profile = _profile.asSharedFlow()
    private var currentProfileId = ""

    private val _chatList = MutableSharedFlow<List<ChatItem>>(1)
    val chatList = _chatList.asSharedFlow()

    private val _eventError = MutableSharedFlow<Int>(0)
    val eventError = _eventError.asSharedFlow()

    fun subscribeProfile() {
        viewModelScope.launch {
            profileRepository.getProfileSubscription().collect {
                currentProfileId = it.id
                val profileUI = it.toUI()
                _profile.emit(profileUI)
            }
        }
    }

    fun deleteChat(chatId: String) {
        val list = _chatList.replayCache.first().filter { it.chatId != chatId }

        viewModelScope.launch {
            val result = profileRepository.deleteChat(
                currentProfileId,
                chatId
            )

            if (result.isFailure) {
                _eventError.emit(R.string.chat_list_delete_chat_error)
            } else {
                _chatList.emit(list)
            }
        }
    }

    fun subscribeChatList() {
        viewModelScope.launch {
            profileRepository
                .getChatListSubscription(currentProfileId)
                .map { list ->
                    list.map {
                        it.toUI(dateUtils)
                    }
                }
                .collect {
                    _chatList.emit(it)
                }
        }
    }
}