package com.example.chit_chat.ui.home.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chit_chat.R
import com.example.chit_chat.domain.interactor.ProfileInteractor
import com.example.chit_chat.ui.mapper.toUI
import com.example.chit_chat.domain.model.Chat
import com.example.chit_chat.utils.DateUtils
import com.example.chit_chat.ui.model.ChatItem
import com.example.chit_chat.ui.model.ProfileUI
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val dateUtils: DateUtils,
    private val profileInteractor: ProfileInteractor
) : ViewModel() {
    private var currentProfileId = ""
    private var chatListDomain = arrayListOf<Chat>()
    private val _profile = MutableSharedFlow<ProfileUI>(1)
    val profile = _profile.asSharedFlow()

    private val _chatList = MutableSharedFlow<List<ChatItem>>(1)
    val chatList = _chatList.asSharedFlow()

    private val _eventError = MutableSharedFlow<Int>(0)
    val eventError = _eventError.asSharedFlow()

    private var chatSubscription: Job? = null

    fun subscribeProfile() {
        viewModelScope.launch {
            profileInteractor.getProfileSubscription().collect {
                currentProfileId = it.id
                val profileUI = it.toUI()
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

