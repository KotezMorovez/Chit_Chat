package com.example.chit_chat.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.chit_chat.common.PREFERENCES
import com.example.chit_chat.common.SharedPrefsService
import com.example.chit_chat.common.SharedPrefsServiceImpl
import com.example.chit_chat.data.repository.AuthRepositoryImpl
import com.example.chit_chat.data.service.auth.AuthService
import com.example.chit_chat.data.service.auth.AuthServiceImpl
import com.example.chit_chat.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
interface OriginalModule {
    @Binds
    @Reusable
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Reusable
    fun bindAuthService(impl: AuthServiceImpl): AuthService

    @Binds
    @Singleton
    fun bindSharedPrefsService(impl: SharedPrefsServiceImpl): SharedPrefsService

}

@Module
class ProviderModule {
    @Provides
    @Singleton
    fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES, MODE_PRIVATE)
    }
}