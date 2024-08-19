package com.example.chit_chat.domain.model

import java.util.ArrayList

data class Profile(
    val id: String,
    val avatar: String? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val contactsIdList: ArrayList<String>
)
