package com.example.chit_chat.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Resources
import com.example.chit_chat.utils.PREFERENCES
import com.example.chit_chat.data.service.profile.SharedPrefsService
import com.example.chit_chat.data.service.profile.SharedPrefsServiceImpl
import com.example.chit_chat.data.repository.AuthRepositoryImpl
import com.example.chit_chat.data.repository.ProfileRepositoryImpl
import com.example.chit_chat.data.service.profile.FirebaseService
import com.example.chit_chat.data.service.profile.FirebaseServiceImpl
import com.example.chit_chat.data.service.profile.ProfileStorage
import com.example.chit_chat.data.service.profile.ProfileStorageImpl
import com.example.chit_chat.data.service.auth.ApiService
import com.example.chit_chat.data.service.auth.ApiServiceImpl
import com.example.chit_chat.data.service.auth.AuthApi
import com.example.chit_chat.data.service.auth.TokenInterceptor
import com.example.chit_chat.data.service.profile.CloudStorageService
import com.example.chit_chat.data.service.profile.CloudStorageServiceImpl
import com.example.chit_chat.domain.interactor.AuthInteractor
import com.example.chit_chat.domain.interactor.AuthInteractorImpl
import com.example.chit_chat.domain.interactor.ContactsInteractor
import com.example.chit_chat.domain.interactor.ContactsInteractorImpl
import com.example.chit_chat.domain.interactor.ProfileInteractor
import com.example.chit_chat.domain.interactor.ProfileInteractorImpl
import com.example.chit_chat.domain.repository.AuthRepository
import com.example.chit_chat.domain.repository.ProfileRepository
import com.example.chit_chat.utils.LogoutHandler
import com.example.chit_chat.utils.LogoutHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface OriginalModule {
    @Binds
    @Reusable
    fun bindContactsInteractor(impl: ContactsInteractorImpl): ContactsInteractor

    @Binds
    @Reusable
    fun bindAuthInteractor(impl: AuthInteractorImpl): AuthInteractor

    @Binds
    @Reusable
    fun bindProfileInteractor(impl: ProfileInteractorImpl): ProfileInteractor

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

    @Binds
    @Reusable
    fun bindCloudStorageService(impl: CloudStorageServiceImpl):CloudStorageService

    @Binds
    @Singleton
    fun bindLogoutHandler(impl: LogoutHandlerImpl): LogoutHandler
}

@Module
@InstallIn(SingletonComponent::class)
class ProviderModule {
    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES, MODE_PRIVATE)
    }

    @Provides
    @Reusable
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }
}

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideAuthApiInstance(
        sharedPrefs: SharedPrefsService,
        logoutHandler: LogoutHandler
    ): AuthApi {
        val tokenInterceptor = TokenInterceptor(sharedPrefs, logoutHandler)
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .readTimeout(AWAIT_TIME, TimeUnit.MILLISECONDS)
                    .connectTimeout(AWAIT_TIME, TimeUnit.MILLISECONDS)
                    .addInterceptor(tokenInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build()
            )
            .build()
            .create(AuthApi::class.java)
    }

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080"
        private const val AWAIT_TIME = 1000L
    }
}