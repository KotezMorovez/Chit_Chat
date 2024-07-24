package com.example.chit_chat.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.chit_chat.common.ACCESS_TOKEN
import com.example.chit_chat.common.PREFERENCES
import com.example.chit_chat.common.SharedPrefsService
import com.example.chit_chat.common.SharedPrefsServiceImpl
import com.example.chit_chat.data.repository.AuthRepositoryImpl
import com.example.chit_chat.data.repository.ProfileRepositoryImpl
import com.example.chit_chat.data.service.FirebaseService
import com.example.chit_chat.data.service.FirebaseServiceImpl
import com.example.chit_chat.data.service.ProfileStorage
import com.example.chit_chat.data.service.ProfileStorageImpl
import com.example.chit_chat.data.service.auth.ApiService
import com.example.chit_chat.data.service.auth.ApiServiceImpl
import com.example.chit_chat.data.service.auth.AuthApi
import com.example.chit_chat.domain.interactor.AuthInteractor
import com.example.chit_chat.domain.interactor.AuthInteractorImpl
import com.example.chit_chat.domain.repository.AuthRepository
import com.example.chit_chat.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
interface OriginalModule {
    @Binds
    @Reusable
    fun bindAuthInteractor(impl: AuthInteractorImpl): AuthInteractor

    @Binds
    @Reusable
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Reusable
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Reusable
    fun bindAuthService(impl: ApiServiceImpl): ApiService

    @Binds
    @Reusable
    fun bindProfileApiService(impl: FirebaseServiceImpl): FirebaseService

    @Binds
    @Reusable
    fun bindSharedPrefsService(impl: SharedPrefsServiceImpl): SharedPrefsService

    @Binds
    @Singleton
    fun bindProfileStorage(impl: ProfileStorageImpl): ProfileStorage
}

@Module
class ProviderModule {
    @Provides
    @Singleton
    fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES, MODE_PRIVATE)
    }
}

@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideAuthApiInstance(sharedPrefs: SharedPreferences): AuthApi {
        val tokenInterceptor = Interceptor { chain ->
            val token = sharedPrefs.getString(ACCESS_TOKEN, "") ?: ""
            val request = chain.request()
            val requestWithToken = request.newBuilder()
                .header(AUTH, "Bearer $token")
                .build()

            return@Interceptor chain.proceed(requestWithToken)
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .readTimeout(AWAIT_TIME, TimeUnit.MILLISECONDS)
                    .connectTimeout(AWAIT_TIME, TimeUnit.MILLISECONDS)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(tokenInterceptor)
                    .build()
            ).build()
            .create(AuthApi::class.java)
    }

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080"
        private const val AUTH = "Authorization"
        private const val AWAIT_TIME = 10000L
    }
}