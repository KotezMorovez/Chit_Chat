package com.example.chit_chat.ui.features.chat_list.dto.chat

import com.example.chit_chat.domain.model.Contact
import com.example.chit_chat.ui.features.create_chat.dto.ContactItem

fun Contact.toUI(): ContactItem {
    return ContactItem(
        id = this.id,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName
    )
}