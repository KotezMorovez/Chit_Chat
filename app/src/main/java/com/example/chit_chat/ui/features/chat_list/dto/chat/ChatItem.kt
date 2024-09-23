package com.example.chit_chat.ui.features.chat_list.dto.chat

data class ChatItem (
    val chatId: String,
    val userName: String,
    val userAvatar: String?,
    val lastMessage: String,
    val lastMessageDate: String
)