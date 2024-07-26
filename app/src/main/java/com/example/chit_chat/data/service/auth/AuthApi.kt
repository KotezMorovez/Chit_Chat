package com.example.chit_chat.data.service.auth

import android.content.SharedPreferences
import com.example.chit_chat.common.ACCESS_TOKEN
import com.example.chit_chat.data.model.LoginRequestEntity
import com.example.chit_chat.data.model.ProfileEntity
import com.example.chit_chat.data.model.SignUpRequestEntity
import com.example.chit_chat.data.model.UserTokenEntity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

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