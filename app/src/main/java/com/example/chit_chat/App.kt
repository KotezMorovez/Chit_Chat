package com.example.chit_chat

import android.app.Application
import android.util.Log
import com.example.chit_chat.data.service.ApiService
import com.example.chit_chat.data.service.ApiServiceImpl
import com.example.chit_chat.di.AppComponentHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Custom app class. Contains basic components initialization.
 */
class App : Application() {
    override fun onCreate() {
        initAppComponent()
        super.onCreate()
    }

    private fun initAppComponent() {
        val component = AppComponentHolder.build(this)
        AppComponentHolder.set(component)
    }
}