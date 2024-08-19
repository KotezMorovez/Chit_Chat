package com.example.chit_chat.data.model.auth

import com.google.gson.annotations.SerializedName

data class UserTokenEntity (
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String
)