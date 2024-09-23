package com.example.chit_chat.ui.features.chat_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.R
import com.example.chit_chat.domain.profile.interactor.ProfileInteractor
import com.example.chit_chat.ui.features.chat_list.dto.chat.toUI
import com.example.chit_chat.domain.profile.dto.Chat
import com.example.chit_chat.utils.DateUtils
import com.example.chit_chat.ui.features.chat_list.dto.chat.ChatItem
import com.example.chit_chat.ui.features.chat_list.dto.profile.ChatListProfileUI
import com.example.chit_chat.ui.features.chat_list.dto.profile.toChatListProfileUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val dateUtils: DateUtils,
    private val profileInteractor: ProfileInteractor
) : ViewModel() {
    private var currentProfileId = ""
    private var chatListDomain = arrayListOf<Chat>()
    private var chatSubscription: Job? = null

    private val _profile = MutableSharedFlow<ChatListProfileUI>(1)
    val profile = _profile.asSharedFlow()

    private val _chatList = MutableSharedFlow<List<ChatItem>>(1)
    val chatList = _chatList.asSharedFlow()

    private val _eventError = MutableSharedFlow<Int>(0)
    val eventError = _eventError.asSharedFlow()

    fun subscribeProfile() {
        viewModelScope.launch {
            profileInteractor.getProfileSubscription().collect {
                currentProfileId = it.id
                val profileUI = it.toChatListProfileUI()
                _profile.emit(profileUI)
            }
        }
    }

    fun leaveChat(chatId: String) {
        val userIdList = chatListDomain.firstOrNull { it.id == chatId }?.userIdList ?: arrayListOf()

        viewModelScope.launch {
            val result = profileInteractor.leaveChat(
                currentProfileId,
                chatId,
                userIdList
            )

            if (result.isFailure) {
                _eventError.emit(R.string.chat_list_delete_chat_error)
            }
        }
    }

    fun subscribeChatList() {
        val id: String? = profileInteractor.getCurrentProfileId()
        if (id != null) {
            chatSubscription?.cancel()
            chatSubscription = viewModelScope.launch {
                profileInteractor.getChatListSubscription(currentProfileId).collect {
                    chatListDomain = ArrayList(it)
                    val chatListUI = chatListDomain
                        .map { chat ->
                            chat.toUI(dateUtils)
                        }

                    _chatList.emit(chatListUI)
                }
            }
        }
    }
}

