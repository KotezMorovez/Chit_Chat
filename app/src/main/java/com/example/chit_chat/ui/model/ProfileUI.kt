package com.example.chit_chat.ui.model

data class ProfileUI(
    val id: String,
    val avatar: String? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val contacts: List<String>
)
