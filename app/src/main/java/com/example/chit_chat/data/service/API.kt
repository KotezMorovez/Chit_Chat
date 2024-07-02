package com.example.chit_chat.data.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Call

interface API {
    @GET("test")
    fun test():Call<Any>

    companion object {
        private var instance: API? = null
        private val interceptor = HttpLoggingInterceptor()

        fun getInstance(): API {
            if (instance == null) {
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

//                Set base URL
                instance = Retrofit.Builder()
                    .baseUrl("https://google.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
                    .build()
                    .create(API::class.java)
            }
            return instance!!
        }
    }
}