package com.example.chit_chat.ui.features.profile_share.dto

import com.example.chit_chat.domain.profile.dto.Profile

fun Profile.toSharingProfileUI(): SharingProfileUI {
    return SharingProfileUI(
        id = this.id,
        avatarUrl = this.avatar
    )
}