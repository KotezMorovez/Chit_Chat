package com.example.chit_chat.domain.mapper

import com.example.chit_chat.domain.model.Profile
import com.example.chit_chat.ui.model.ProfileUI

fun Profile.toUI(): ProfileUI {
    return ProfileUI(
        email = this.email,
        avatar = this.avatar,
        firstName = this.firstName,
        lastName = this.lastName
    )
}