package com.example.chit_chat.ui.mapper

import com.example.chit_chat.domain.model.Profile
import com.example.chit_chat.ui.model.ProfileUI

fun ProfileUI.toDomain(): Profile {
    return Profile(
        id = this.id,
        email = this.email,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName,
        contacts = this.contacts
    )
}

fun Profile.toUI(): ProfileUI {
    return ProfileUI(
        id = this.id,
        email = this.email,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName,
        contacts = this.contacts
    )
}