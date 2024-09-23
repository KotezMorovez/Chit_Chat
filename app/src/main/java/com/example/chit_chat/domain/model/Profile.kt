package com.example.chit_chat.domain.model

data class Profile(
    val id: String,
    val avatar: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val contacts: List<String>
)
