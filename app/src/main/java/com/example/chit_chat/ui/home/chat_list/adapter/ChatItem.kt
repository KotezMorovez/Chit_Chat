package com.example.chit_chat.ui.home.chat_list.adapter

data class ChatItem (
    val chatUserId: String,
    val chatUserName: String,
    val chatUserAvatar: String?,
    val chatLastMessage: String,
    val chatLastMessageDate: String,
    val chatMessagesCount: String
)