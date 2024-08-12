package com.example.chit_chat.ui.home.chat_list.adapter

data class ChatItem (
    val chatId: String,
    val userName: String,
    val userAvatar: String?,
    val lastMessage: String,
    val lastMessageDate: String
)