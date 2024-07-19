package com.example.chit_chat.ui.home.chat_list.adapter

data class ChatItem (
    val userId: String,
    val userName: String,
    val userAvatar: String?,
    val lastMessage: String,
    val lastMessageDate: String,
    val messagesCount: String
)