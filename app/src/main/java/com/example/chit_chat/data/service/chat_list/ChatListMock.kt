package com.example.chit_chat.data.service.chat_list

import com.example.chit_chat.ui.home.chat_list.adapter.ChatItem
import java.util.UUID

object ChatListMock {
    fun getChatList(): ArrayList<ChatItem> {
        return arrayListOf(
            ChatItem(
                userId = UUID.randomUUID().toString(),
                userName = "Test1",
                userAvatar = "https://i.pinimg.com/474x/55/1f/ff/551fff636303fb8a696c213736ddc09e.jpg",
                lastMessage = "message 1231455546545456",
                lastMessageDate = "5:45",
                messagesCount = "1",
            ),
            ChatItem(
                userId = UUID.randomUUID().toString(),
                userName = "Test2",
                userAvatar = null,
                lastMessage = "message 1231455546545456",
                lastMessageDate = "13:45",
                messagesCount = "3",
            ),
            ChatItem(
                userId = UUID.randomUUID().toString(),
                userName = "Test3",
                userAvatar = null,
                lastMessage = "message 1231455546545456",
                lastMessageDate = "Чт",
                messagesCount = "145",
            )
        )
    }
}