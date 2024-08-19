package com.example.chit_chat.ui.mapper

import com.example.chit_chat.domain.model.Chat
import com.example.chit_chat.ui.model.ChatItem
import com.example.chit_chat.utils.DateUtils

fun Chat.toUI(dateUtils: DateUtils): ChatItem {
    return ChatItem(
        chatId = this.id,
        userName = this.chatName,
        userAvatar = this.chatAvatar,
        lastMessage = this.lastMessage,
        lastMessageDate = dateUtils.getElapsedTime(this.lastMessageDate)
    )
}