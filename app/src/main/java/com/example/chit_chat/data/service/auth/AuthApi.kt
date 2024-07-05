package com.example.chit_chat.data.service.auth

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Call

interface AuthApi {
    @GET("test")
    fun test():Call<Any>

    companion object {
        private const val BASE_URL = "https://google.com/"
        private var instance: AuthApi? = null
        private val interceptor = HttpLoggingInterceptor()

        fun getInstance(): AuthApi {
            if (instance == null) {
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)  // TODO: Set base URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
                    .build()
                    .create(AuthApi::class.java)
            }
            return instance!!
        }
    }
}