package com.example.chit_chat.ui.features.chat_list.dto.profile

import com.example.chit_chat.domain.profile.dto.Profile

fun Profile.toChatListProfileUI(): ChatListProfileUI {
    return ChatListProfileUI(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName
    )
}