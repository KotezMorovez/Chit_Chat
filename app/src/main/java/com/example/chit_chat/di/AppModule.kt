package com.example.chit_chat.di

import com.example.chit_chat.data.repository.AuthRepository
import com.example.chit_chat.data.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface OriginalModule {
    @Binds
    @Reusable
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}