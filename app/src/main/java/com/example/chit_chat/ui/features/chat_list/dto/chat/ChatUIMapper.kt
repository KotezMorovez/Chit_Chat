package com.example.chit_chat.ui.features.chat_list.dto.chat

import com.example.chit_chat.domain.profile.dto.Chat
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