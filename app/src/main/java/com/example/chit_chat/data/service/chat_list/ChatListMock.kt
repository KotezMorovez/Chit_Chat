package com.example.chit_chat.data.service.chat_list

import com.example.chit_chat.ui.home.chat_list.adapter.ChatItem
import java.util.UUID

object ChatListMock {
    fun getChatList(): ArrayList<ChatItem> {
        return arrayListOf(
            ChatItem(
                chatUserId = UUID.randomUUID().toString(),
                chatUserName = "Test1",
                chatUserAvatar = "https://i.pinimg.com/474x/55/1f/ff/551fff636303fb8a696c213736ddc09e.jpg",
                chatLastMessage = "message 1231455546545456",
                chatLastMessageDate = "5:45",
                chatMessagesCount = "1",
            ),
            ChatItem(
                chatUserId = UUID.randomUUID().toString(),
                chatUserName = "Test2",
                chatUserAvatar = null,
                chatLastMessage = "message 1231455546545456",
                chatLastMessageDate = "13:45",
                chatMessagesCount = "3",
            ),
            ChatItem(
                chatUserId = UUID.randomUUID().toString(),
                chatUserName = "Test3",
                chatUserAvatar = null,
                chatLastMessage = "message 1231455546545456",
                chatLastMessageDate = "Чт",
                chatMessagesCount = "145",
            )
        )
    }
}