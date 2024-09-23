package com.example.chit_chat.data.auth.service

import com.example.chit_chat.data.auth.dto.LoginRequestEntity
import com.example.chit_chat.data.auth.dto.SignUpRequestEntity
import com.example.chit_chat.data.auth.dto.UserTokenEntity
import com.example.chit_chat.data.profile.dto.profile.ProfileEntity
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    fun login(@Body request: LoginRequestEntity): Call<UserTokenEntity>

    @POST("register")
    suspend fun register(@Body request: SignUpRequestEntity): UserTokenEntity

    @GET("profile")
    fun getProfile(): Call<ProfileEntity>

    @POST
    fun refreshToken(@Body request: UserTokenEntity): Call<UserTokenEntity>
}