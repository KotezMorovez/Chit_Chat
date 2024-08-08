package com.example.chit_chat.data.model

data class ChatEntity (
    val id: String,
    val chatName: String?,
    val lastMessage: String,
    val lastMessageDate: Long,
    val userIdList: ArrayList<String>,
    val chatAvatar: String?
)