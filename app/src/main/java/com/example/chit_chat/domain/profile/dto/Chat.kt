package com.example.chit_chat.domain.profile.dto

import org.joda.time.DateTime

data class Chat(
    val id: String,
    val chatName: String,
    val lastMessage: String,
    val lastMessageDate: DateTime,
    val userIdList: ArrayList<String>,
    val chatAvatar: String?
) {
}
