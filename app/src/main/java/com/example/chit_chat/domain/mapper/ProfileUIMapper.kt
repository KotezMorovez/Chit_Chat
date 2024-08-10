package com.example.chit_chat.domain.mapper

import com.example.chit_chat.domain.model.Chat
import com.example.chit_chat.domain.model.Profile
import com.example.chit_chat.ui.common.DateUtils
import com.example.chit_chat.ui.home.chat_list.adapter.ChatItem
import com.example.chit_chat.ui.model.ProfileUI

fun ProfileUI.toDomain(): Profile {
    return Profile(
        id = this.id,
        email = this.email,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName
    )
}

fun Profile.toUI(): ProfileUI {
    return ProfileUI(
        id = this.id,
        email = this.email,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName
    )
}

fun Chat.toUI(dateUtils: DateUtils): ChatItem {
    return ChatItem(
        chatId = this.id,
        userName = this.chatName,
        userAvatar = this.chatAvatar,
        lastMessage = this.lastMessage,
        lastMessageDate = dateUtils.getElapsedTime(this.lastMessageDate)
    )
}