package com.example.chit_chat.ui.features.settings.dto

import com.example.chit_chat.domain.profile.dto.Profile

fun Profile.toSettingsProfileUI(): SettingsProfileUI {
    return SettingsProfileUI(
        id = this.id,
        avatar = this.avatar
    )
}