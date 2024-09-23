package com.example.chit_chat.data.auth.dto

import com.google.gson.annotations.SerializedName

data class SignUpRequestEntity(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("user_name")
    val userName: String,
    val email: String,
    val password: String
)
