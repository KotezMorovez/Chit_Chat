package com.example.chit_chat.data.service.auth

import com.example.chit_chat.data.model.auth.LoginRequestEntity
import com.example.chit_chat.data.model.ProfileEntity
import com.example.chit_chat.data.model.auth.SignUpRequestEntity
import com.example.chit_chat.data.model.auth.UserTokenEntity
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    fun login(@Body request: LoginRequestEntity): Call<UserTokenEntity>

    @POST("register")
    fun register(@Body request: SignUpRequestEntity): Call<UserTokenEntity>

    @GET("profile")
    fun getProfile(): Call<ProfileEntity>

    @POST
    fun refreshToken(@Body request: UserTokenEntity): Call<UserTokenEntity>
}