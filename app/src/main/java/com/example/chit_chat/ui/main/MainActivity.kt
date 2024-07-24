package com.example.chit_chat.ui.main

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.chit_chat.R
import com.example.chit_chat.di.AppComponentHolder
import com.example.chit_chat.ui.common.LogoutHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var logoutHandler: LogoutHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        AppComponentHolder.get().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeNavData()
    }

    @MainThread
    @CallSuper
    fun logout() {
        this.findNavController(R.id.nav_host).setGraph(R.navigation.nav_graph)
    }

    private fun observeNavData() {
        this.lifecycleScope.launch {
            launch {
                logoutHandler.event.collect {
                    logout()
                }
            }
        }
    }
}