package com.example.chit_chat.ui.mapper

import com.example.chit_chat.domain.model.Contact
import com.example.chit_chat.ui.model.ContactItem

fun Contact.toUI(): ContactItem {
    return ContactItem(
        id = this.id,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName
    )
}