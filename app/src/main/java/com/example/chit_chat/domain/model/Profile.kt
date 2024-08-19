package com.example.chit_chat.domain.model

data class Profile(
    val id: String,
    val avatar: String? = null,
    val email: String,
    val firstName: String,
    val lastName: String
)
