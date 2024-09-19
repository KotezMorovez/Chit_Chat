package com.example.chit_chat.data.model

import com.google.gson.annotations.SerializedName

data class ProfileEntity(
    val id: String,
    val email: String,
    val avatar: String?,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val contacts: List<String>
)
