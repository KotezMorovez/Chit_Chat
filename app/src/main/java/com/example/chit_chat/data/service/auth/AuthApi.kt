package com.example.chit_chat.data.service.auth

import android.content.SharedPreferences
import android.icu.number.IntegerWidth
import com.example.chit_chat.common.ACCESS_TOKEN
import com.example.chit_chat.common.SharedPrefsService
import com.example.chit_chat.data.model.LoginRequestEntity
import com.example.chit_chat.data.model.ProfileEntity
import com.example.chit_chat.data.model.UserTokenEntity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("login")
    fun login(@Body request: LoginRequestEntity): Call<UserTokenEntity>

    @GET("profile")
    fun getProfile(): Call<ProfileEntity>

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080"
        private var instance: AuthApi? = null

        fun getInstance(sharedPrefs: SharedPreferences): AuthApi {
            val tokenInterceptor = Interceptor { chain ->
                val token = sharedPrefs.getString(ACCESS_TOKEN, "") ?: ""
                val request = chain.request()
                val requestWithToken = request.newBuilder()
                    .header(ACCESS_TOKEN,  token)
                    .build()

                return@Interceptor chain.proceed(requestWithToken)
            }

            if (instance == null) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                instance = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).client(
                        OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .addInterceptor(tokenInterceptor)
                            .build()
                    ).build().create(AuthApi::class.java)
            }
            return instance!!
        }
    }
}